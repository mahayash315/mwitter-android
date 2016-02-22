package jp.ac.nitech.itolab.mwitter.ui;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import jp.ac.nitech.itolab.mwitter.R;
import jp.ac.nitech.itolab.mwitter.model.entity.Tweet;

/**
 * A fragment representing a single Tweet detail screen.
 * This fragment is either contained in a {@link TweetListActivity}
 * in two-pane mode (on tablets) or a {@link TweetDetailActivity}
 * on handsets.
 */
public class TweetDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item TID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_TID = "item_tid";

    /**
     * The content this fragment is presenting.
     */
    private Tweet mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_TID)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Tweet.findByTid(getArguments().getLong(ARG_ITEM_TID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.user.getDispName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tweet_detail, container, false);

        // Show the content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.tweet_detail)).setText(mItem.content);
        }

        return rootView;
    }
}
