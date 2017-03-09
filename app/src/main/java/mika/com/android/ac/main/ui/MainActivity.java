/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.main.ui;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
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
import mika.com.android.ac.R;
import mika.com.android.ac.account.info.AccountContract;
import mika.com.android.ac.articlelist.content.ListSort;
import mika.com.android.ac.home.HomeFragment;
import mika.com.android.ac.navigation.ui.NavigationFragment;
import mika.com.android.ac.network.NetWorkStateReceiver;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.notification.service.AlarmPoll;
import mika.com.android.ac.notification.service.PollingService;
import mika.com.android.ac.notification.ui.NotificationListFragment;
import mika.com.android.ac.quote.ui.QuoteFragment;
import mika.com.android.ac.ui.ActionItemBadge;
import mika.com.android.ac.ui.DrawerManager;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.TransitionUtils;

public class MainActivity extends AppCompatActivity implements
        DrawerManager,
        NotificationListFragment.UnreadNotificationCountListener,
        NavigationFragment.OnNavigationMenuClickListener,
        QuoteFragment.OnQuoteInteractionListener,
        PollingService.OnNewMentionListener {

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.container)
    FrameLayout mContainerLayout;

    private MenuItem mNotificationMenu;
    private int mUnreadNotificationCount;
    private NavigationFragment mNavigationFragment;
    //    private NotificationListFragment mNotificationListFragment;
    private boolean isRemoveAcerinfo;
    private HomeFragment homeFragment;
    private NetWorkStateReceiver netWorkStateReceiver;

    public ServiceConnection pushServiceConn;
    public PollingService.PushBinder pushBinder;

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
        TransitionUtils.setupTransitionBeforeDecorate(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        // 注册监听联网状态广播
        doRegisterReceiver();
        TransitionUtils.setupTransitionAfterSetContentView(this);
        ButterKnife.bind(this);

        bindPush();
        //noinspection deprecation
//        ScalpelHelperFragment.attachTo(this);

        mNavigationFragment = FragmentUtils.findById(this, R.id.navigation_fragment);
        homeFragment = HomeFragment.newInstance();
        if (savedInstanceState == null) {
            FragmentUtils.add(homeFragment, this, R.id.container);
        }
    }

    private void doRegisterReceiver() {
        netWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
    }

    private void bindPush() {
        pushServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                pushBinder = (PollingService.PushBinder) service;
                pushBinder.getPollingService().setNewMentionListener(MainActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        AlarmPoll.startPolling(this, pushServiceConn, 60);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mNotificationMenu = menu.findItem(R.id.action_notification);

        ActionItemBadge.setup(mNotificationMenu, R.drawable.notifications_icon_white_24dp,
                mUnreadNotificationCount, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavigationFragment.getView());
                return true;
            case R.id.action_notification:
//                startActivity();
                return true;
            case R.id.action_top:
                homeFragment.currentGoTop();
                return true;
            case R.id.action_pop:
                if (homeFragment != null)
                    homeFragment.onArticleSortChanged(ListSort.Sort.HOT);
                return true;
            case R.id.action_new_comment:
                if (homeFragment != null)
                    homeFragment.onArticleSortChanged(ListSort.Sort.NEW_REPLY);
                return true;
            case R.id.action_news:
                if (homeFragment != null)
                    homeFragment.onArticleSortChanged(ListSort.Sort.NEWS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AccountContract.isLogin()) {
            if (!isRemoveAcerinfo) {
                mNavigationFragment.onUserInfoChanged(0, null);
                isRemoveAcerinfo = true;
            }
        } else {
            isRemoveAcerinfo = false;
        }
    }

    public Acer getAcer() {
        return mNavigationFragment.getAcer();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationFragment.getView() == null) return;
        if (mDrawerLayout.isDrawerOpen(mNavigationFragment.getView())) {
            mDrawerLayout.closeDrawer(mNavigationFragment.getView());
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

    public void refreshNotificationList() {
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(netWorkStateReceiver);
        AlarmPoll.cancelPolling(this, pushServiceConn);
        super.onDestroy();
    }

    @Override
    public void onHomeClicked() {

    }

    @Override
    public void onStarClicked() {

    }

    @Override
    public void onQuoteClicked() {
    }

    @Override
    public void onMessageClicked() {
    }

    @Override
    public void onNewMention(int count) {
        mUnreadNotificationCount = count;
        if (mNotificationMenu != null) {
            ActionItemBadge.update(mNotificationMenu, mUnreadNotificationCount);
        }
        mNavigationFragment.setQuoteCount(count);
    }
}
