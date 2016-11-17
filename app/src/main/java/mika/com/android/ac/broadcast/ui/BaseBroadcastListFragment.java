/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import mika.com.android.ac.broadcast.content.BroadcastListResource;
import mika.com.android.ac.broadcast.content.LikeBroadcastManager;
import mika.com.android.ac.broadcast.content.RebroadcastBroadcastManager;
import mika.com.android.ac.link.NotImplementedManager;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.ui.AppBarManager;
import mika.com.android.ac.ui.FriendlyFloatingActionButton;
import mika.com.android.ac.ui.FriendlySwipeRefreshLayout;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.ui.OnVerticalScrollWithPagingTouchSlopListener;
import mika.com.android.ac.util.CardUtils;
import mika.com.android.ac.util.CheatSheetUtils;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.RecyclerViewUtils;
import mika.com.android.ac.util.ToastUtil;
import mika.com.android.ac.util.TransitionUtils;
import mika.com.android.ac.util.ViewUtils;

public abstract class BaseBroadcastListFragment extends Fragment
        implements BroadcastListResource.Listener, BroadcastAdapter.Listener {

    @BindView(mika.com.android.ac.R.id.swipe_refresh)
    FriendlySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(mika.com.android.ac.R.id.broadcast_list)
    RecyclerView mBroadcastList;
    @BindView(mika.com.android.ac.R.id.progress)
    ProgressBar mProgress;
    @BindView(mika.com.android.ac.R.id.send)
    FriendlyFloatingActionButton mSendFab;

    private BroadcastListResource mBroadcastListResource;

    private BroadcastAdapter mBroadcastAdapter;
    private LoadMoreAdapter mAdapter;

    protected BaseBroadcastListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mika.com.android.ac.R.layout.broadcast_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        CustomTabsHelperFragment.attachTo(this);

        mBroadcastListResource = onAttachBroadcastListResource();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });

        mBroadcastList.setHasFixedSize(true);
        mBroadcastList.setItemAnimator(new NoChangeAnimationItemAnimator());
        // Always use StaggeredGridLayoutManager so that instance state can be saved.
        int columnCount = CardUtils.getColumnCount(activity);
        mBroadcastList.setLayoutManager(new StaggeredGridLayoutManager(columnCount,
                StaggeredGridLayoutManager.VERTICAL));
        mBroadcastAdapter = new BroadcastAdapter(mBroadcastListResource.get(), this);
        mAdapter = new LoadMoreAdapter(mika.com.android.ac.R.layout.load_more_card_item, mBroadcastAdapter);
        mBroadcastList.setAdapter(mAdapter);
        final AppBarManager appBarManager = (AppBarManager) getParentFragment();
        mBroadcastList.addOnScrollListener(
                new OnVerticalScrollWithPagingTouchSlopListener(activity) {
                    @Override
                    public void onScrolled(int dy) {
                        if (!RecyclerViewUtils.hasFirstChildReachedTop(mBroadcastList)) {
                            onShow();
                        }
                    }
                    @Override
                    public void onScrolledUp() {
                        onShow();
                    }
                    private void onShow() {
                        appBarManager.showAppBar();
                        mSendFab.show();
                    }
                    @Override
                    public void onScrolledDown() {
                        if (RecyclerViewUtils.hasFirstChildReachedTop(mBroadcastList)) {
                            appBarManager.hideAppBar();
                            mSendFab.hide();
                        }
                    }
                    @Override
                    public void onScrolledToBottom() {
                        mBroadcastListResource.load(true);
                    }
                });

        updateRefreshing();

        CheatSheetUtils.setup(mSendFab);
        mSendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendBroadcast();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBroadcastListResource.detach();
    }

    @Override
    public void onLoadBroadcastListStarted(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadBroadcastListFinished(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadBroadcastListError(int requestCode, VolleyError error) {
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onBroadcastListChanged(int requestCode, List<Broadcast> newBroadcastList) {
        mBroadcastAdapter.replace(newBroadcastList);
    }

    @Override
    public void onBroadcastListAppended(int requestCode, List<Broadcast> appendedBroadcastList) {
        mBroadcastAdapter.addAll(appendedBroadcastList);
    }

    @Override
    public void onBroadcastChanged(int requestCode, int position, Broadcast newBroadcast) {
        mBroadcastAdapter.set(position, newBroadcast);
    }

    @Override
    public void onBroadcastRemoved(int requestCode, int position) {
        mBroadcastAdapter.remove(position);
    }

    @Override
    public void onBroadcastWriteFinished(int requestCode, int position) {
        mBroadcastAdapter.notifyItemChanged(position);
    }

    @Override
    public void onBroadcastWriteStarted(int requestCode, int position) {
        mBroadcastAdapter.notifyItemChanged(position);
    }

    private void updateRefreshing() {
        boolean loading = mBroadcastListResource.isLoading();
        boolean empty = mBroadcastListResource.isEmpty();
        boolean loadingMore = mBroadcastListResource.isLoadingMore();
        mSwipeRefreshLayout.setRefreshing(loading && (mSwipeRefreshLayout.isRefreshing() || !empty)
                && !loadingMore);
        ViewUtils.setVisibleOrGone(mProgress, loading && empty);
        mAdapter.setProgressVisible(loading && !empty && loadingMore);
    }

    @Override
    public void onLikeBroadcast(Broadcast broadcast, boolean like) {
        LikeBroadcastManager.getInstance().write(broadcast, like, getActivity());
    }

    @Override
    public void onRebroadcastBroadcast(Broadcast broadcast, boolean rebroadcast) {
        RebroadcastBroadcastManager.getInstance().write(broadcast, rebroadcast, getActivity());
    }

    @Override
    public void onCommentBroadcast(Broadcast broadcast, View sharedView) {
        // Open ime for comment if there is none; otherwise we always let the user see what others
        // have already said first, to help to make the world a better place.
        openBroadcast(broadcast, sharedView, broadcast.canComment() && broadcast.commentCount == 0);
    }

    @Override
    public void onOpenBroadcast(Broadcast broadcast, View sharedView) {
        openBroadcast(broadcast, sharedView, false);
    }

    private void openBroadcast(Broadcast broadcast, View sharedView, boolean showSendComment) {
        Activity activity = getActivity();
        Intent intent = BroadcastActivity.makeIntent(broadcast, showSendComment,
                activity.getTitle().toString(), activity);
        Bundle options = TransitionUtils.makeActivityOptionsBundle(activity, sharedView);
        ActivityCompat.startActivity(activity, intent, options);
    }

    protected void setPaddingTop(int paddingTop) {
        mSwipeRefreshLayout.setProgressViewOffset(paddingTop);
        mBroadcastList.setPadding(mBroadcastList.getPaddingLeft(), paddingTop,
                mBroadcastList.getPaddingRight(), mBroadcastList.getPaddingBottom());
    }

    protected abstract BroadcastListResource onAttachBroadcastListResource();

    protected void onSwipeRefresh() {
        mBroadcastListResource.load(false);
    }

    protected void onSendBroadcast() {
        NotImplementedManager.sendBroadcast(null, getActivity());
    }
}
