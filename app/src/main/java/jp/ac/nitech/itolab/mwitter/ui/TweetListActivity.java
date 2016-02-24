package jp.ac.nitech.itolab.mwitter.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;

import jp.ac.nitech.itolab.mwitter.R;
import jp.ac.nitech.itolab.mwitter.io.TweetHandler;
import jp.ac.nitech.itolab.mwitter.model.entity.Tweet;
import jp.ac.nitech.itolab.mwitter.service.TweetService;
import jp.ac.nitech.itolab.mwitter.ui.widget.CursorRecyclerViewAdapter;

import java.io.IOException;
import java.util.List;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGD;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

/**
 * An activity representing a list of Tweets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TweetDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TweetListActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = makeLogTag(TweetListActivity.class);

    private static final int REQUEST_CODE_TWEET_CREATE = 101;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);

        setupSwipeRefreshLayout();
        setupRecyclerView();
        setupFloatingActionButton();

        if (findViewById(R.id.tweet_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        LOGD(TAG, "size="+new Select().from(Tweet.class).execute().size());
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout == null) {
            return;
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.tweet_list);
        if (mRecyclerView == null) {
            return;
        }
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, null));
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void setupFloatingActionButton() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        if (mFloatingActionButton == null) {
            return;
        }
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivityForResult(new Intent(TweetListActivity.this, TweetCreateActivity.class), REQUEST_CODE_TWEET_CREATE);
            }
        });
    }

    @Override
    public void onRefresh() {
        Intent intent = new Intent(this, TweetService.class);
        intent.setAction(TweetService.ACTION_UPDATE);
        intent.putExtra(TweetService.INTENT_KEY_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                LOGD(TAG, "onReceiveResult: " + resultCode + ", " + resultData); // TODO: implement this

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(mRecyclerView, "List updated.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
        startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ContentProvider.createUri(Tweet.class, null),
                null, null, null, Tweet.ORDER_BY_CREATION);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((SimpleItemRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleItemRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_TWEET_CREATE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String content = extras.getString(TweetCreateActivity.RESULT_KEY_CONTENT);
                    processPostTweet(content);
                } else {
                    // ツイートしない
                    LOGD(TAG, "Tweet not posted. resultCode="+resultCode);
                }
                break;
        }
    }

    private void processPostTweet(String content) {
        final Tweet tweet = Tweet.createNew(content);
        final View view = findViewById(android.R.id.content);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                Snackbar.make(view, "Posting", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Tweet newTweet = Tweet.fromResponse(new TweetHandler(TweetListActivity.this).postTweet(tweet.toRequest()));
                    LOGD(TAG, "newTweet"+newTweet);
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGE(TAG, "error", e);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                Snackbar.make(view, "Tweet posted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }.execute();
    }

    public class SimpleItemRecyclerViewAdapter
            extends CursorRecyclerViewAdapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        public SimpleItemRecyclerViewAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tweet_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            final Tweet item = Tweet.fromCursor(cursor);
            viewHolder.mItem = item;
            viewHolder.mIdView.setText(item.user.getDispName());
            viewHolder.mContentView.setText(item.content);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(TweetDetailFragment.ARG_ITEM_TID, item.tid);
                        TweetDetailFragment fragment = new TweetDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.tweet_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TweetDetailActivity.class);
                        intent.putExtra(TweetDetailFragment.ARG_ITEM_TID, item.tid);

                        context.startActivity(intent);
                    }
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Tweet mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
