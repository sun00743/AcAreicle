/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.main.ui;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.BuildConfig;
import mika.com.android.ac.DouyaApplication;
import mika.com.android.ac.home.HomeFragment;
import mika.com.android.ac.navigation.ui.NavigationFragment;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.notification.ui.NotificationListFragment;
import mika.com.android.ac.scalpel.ScalpelHelperFragment;
import mika.com.android.ac.ui.ActionItemBadge;
import mika.com.android.ac.ui.DrawerManager;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.TransitionUtils;

public class MainActivity extends AppCompatActivity
        implements DrawerManager, NotificationListFragment.UnreadNotificationCountListener {

    @BindView(mika.com.android.ac.R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(mika.com.android.ac.R.id.notification_list_drawer)
    View mNotificationDrawer;
    @BindView(mika.com.android.ac.R.id.container)
    FrameLayout mContainerLayout;

    private MenuItem mNotificationMenu;
    private int mUnreadNotificationCount;
    private NavigationFragment mNavigationFragment;
//    private NotificationListFragment mNotificationListFragment;
    private boolean isRemoveAcerinfo;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        setTheme(mika.com.android.ac.R.style.Theme_AcWen_MainActivity);

        TransitionUtils.setupTransitionBeforeDecorate(this);

        super.onCreate(savedInstanceState);

//        if (!AccountUtils.ensureAccountAvailability(this)) {
//            return;
//        }

        setContentView(mika.com.android.ac.R.layout.main_activity);
        TransitionUtils.setupTransitionAfterSetContentView(this);
        ButterKnife.bind(this);

        ScalpelHelperFragment.attachTo(this);

        mNavigationFragment = FragmentUtils.findById(this, mika.com.android.ac.R.id.navigation_fragment);
//        mNotificationListFragment = FragmentUtils.findById(this, R.id.notification_list_fragment);
//        mNotificationListFragment.setUnreadNotificationCountListener(this);
        homeFragment = HomeFragment.newInstance();
        if (savedInstanceState == null) {
            FragmentUtils.add(homeFragment, this, mika.com.android.ac.R.id.container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(mika.com.android.ac.R.menu.main, menu);
        mNotificationMenu = menu.findItem(mika.com.android.ac.R.id.action_notification);

        ActionItemBadge.setup(mNotificationMenu, mika.com.android.ac.R.drawable.notifications_icon_white_24dp,
                mUnreadNotificationCount, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavigationFragment.getView());
                return true;
            case mika.com.android.ac.R.id.action_notification:
//                mNotificationListFragment.refresh();
                mDrawerLayout.openDrawer(mNotificationDrawer);
                return true;
            case mika.com.android.ac.R.id.action_top:
                homeFragment.currentGoTop();
//                NotImplementedManager.openDoumail(this);
                return true;
//            case R.id.action_search:
//                NotImplementedManager.openSearch(this);

//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!DouyaApplication.LOGIN){
            if(!isRemoveAcerinfo) {
                mNavigationFragment.onUserInfoChanged(0, null);
                isRemoveAcerinfo = true;
            }
        }else{
            isRemoveAcerinfo = false;
        }
    }

    public Acer getAcer(){
        return mNavigationFragment.getAcer();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mNavigationFragment.getView())) {
                mDrawerLayout.closeDrawer(mNavigationFragment.getView());
        }
        else if (mDrawerLayout.isDrawerOpen(mNotificationDrawer)) {
            mDrawerLayout.closeDrawer(mNotificationDrawer);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);

        TransitionUtils.setupTransitionForAppBar(this);
    }

    @Override
    public void openDrawer(View drawerView) {
        mDrawerLayout.openDrawer(drawerView);
    }

    @Override
    public void closeDrawer(View drawerView) {
        mDrawerLayout.closeDrawer(drawerView);
    }

    @Override
    public void onUnreadNotificationUpdate(int count) {
        mUnreadNotificationCount = count;
        if (mNotificationMenu != null) {
            ActionItemBadge.update(mNotificationMenu, mUnreadNotificationCount);
        }
    }

    private void onShowSettings() {

    }

    public void refreshNotificationList() {
//        mNotificationListFragment.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
