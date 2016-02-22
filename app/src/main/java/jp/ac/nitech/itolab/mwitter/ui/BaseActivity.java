package jp.ac.nitech.itolab.mwitter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import jp.ac.nitech.itolab.mwitter.R;
import jp.ac.nitech.itolab.mwitter.model.entity.User;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = makeLogTag(BaseActivity.class);

    private static final int NAVIGATION_VIEW_HEADER_IDX_ACCOUNT = 0;

    // ActionBar
    private Toolbar mActionBarToolbar;

    // Navigation Drawer
    private DrawerLayout mDrawerLayout;

    // Navigation View
    private NavigationView mNavigationView;

    // Navigation View Account View
    private View mDrawerAccountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        setupNavView();
        setupAccountBox();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, getActionBarToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupNavView() {
        if (mDrawerLayout == null) {
            return;
        }

        mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.nav_view);
        if (mNavigationView == null) {
            return;
        }

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setupAccountBox() {
        if (mNavigationView == null) {
            return;
        }

        mDrawerAccountView = mNavigationView.getHeaderView(NAVIGATION_VIEW_HEADER_IDX_ACCOUNT);
        if (mDrawerAccountView == null) {
            return;
        }

        final ImageView ivAccount = (ImageView) mDrawerAccountView.findViewById(R.id.imageView);
        final TextView tvName = (TextView) mDrawerAccountView.findViewById(R.id.name);
        final TextView tvEmail = (TextView) mDrawerAccountView.findViewById(R.id.email);

        User me = User.getMe(this);
        if (me == null) {
            // TODO: 15/02/2016 icon を変える
            tvName.setText("UNKNOWN");
            tvEmail.setText("UNKNOWN");
        } else {
            // TODO: 15/02/2016 icon を変える
            tvName.setText(me.getName());
            tvEmail.setText(me.dispName);
        }

        // FIXME: 15/02/2016 起動時だけにする. 今はActivityが作られる度に取りに行ってる.
        final Context context = this;
        new AsyncTask<Void,Void,User>() {
            @Override
            protected User doInBackground(Void... params) {
                try {
                    return User.updateMe(context);
                } catch (IOException e) {
                    LOGE(TAG, "Could not retrieve User.me", e);
                }
                return null;
            }
            @Override
            protected void onPostExecute(User me) {
                if (me == null) {
                    // TODO: 15/02/2016 icon を変える
                    tvName.setText("UNKNOWN");
                    tvEmail.setText("UNKNOWN");
                } else {
                    // TODO: 15/02/2016 icon を変える
                    tvName.setText(me.getName());
                    tvEmail.setText(me.dispName);
                }
            }
        }.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_timeline) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }
}
