/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.user.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.ui.OnVerticalScrollListener;
import mika.com.android.ac.user.content.BaseUserListResource;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;
import mika.com.android.ac.util.ViewUtils;

public abstract class UserListFragment extends Fragment implements BaseUserListResource.Listener {

    @BindView(mika.com.android.ac.R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(mika.com.android.ac.R.id.user_list)
    RecyclerView mUserList;
    @BindView(mika.com.android.ac.R.id.progress)
    ProgressBar mProgress;

    private BaseUserAdapter mUserAdapter;
    private LoadMoreAdapter mAdapter;

    private BaseUserListResource mUserListResource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mika.com.android.ac.R.layout.user_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserListResource = onAttachUserListResource();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListResource.load(false);
            }
        });

        // TODO: OK?
        //mUserList.setHasFixedSize(true);
        mUserList.setItemAnimator(new NoChangeAnimationItemAnimator());
        mUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUserAdapter = onCreateAdapter();
        if (mUserListResource.has()) {
            //noinspection unchecked
            mUserAdapter.replace(mUserListResource.get());
        }
        mAdapter = new LoadMoreAdapter(mika.com.android.ac.R.layout.load_more_item, mUserAdapter);
        mUserList.setAdapter(mAdapter);
        mUserList.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledToBottom() {
                mUserListResource.load(true);
            }
        });

        updateRefreshing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mUserListResource.detach();
    }

    protected abstract BaseUserListResource onAttachUserListResource();

    @Override
    public void onLoadUserListStarted(int requestCode, boolean loadMore) {
        updateRefreshing();
    }

    @Override
    public void onLoadUserListFinished(int requestCode, boolean loadMore) {
        updateRefreshing();
    }

    @Override
    public void onLoadUserListError(int requestCode, VolleyError error) {
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onUserListChanged(int requestCode, List<User> newUserList) {
        mUserAdapter.replace(newUserList);
        //noinspection unchecked
        onUserListUpdated(mUserListResource.get());
    }

    @Override
    public void onUserListAppended(int requestCode, List<User> appendedUserList) {
        mUserAdapter.addAll(appendedUserList);
        //noinspection unchecked
        onUserListUpdated(mUserListResource.get());
    }

    abstract protected BaseUserAdapter onCreateAdapter();

    protected void onUserListUpdated(List<User> userList) {}

    private void updateRefreshing() {
        boolean loading = mUserListResource.isLoading();
        boolean empty = mUserListResource.isEmpty();
        boolean loadingMore = mUserListResource.isLoadingMore();
        mSwipeRefreshLayout.setRefreshing(loading && (mSwipeRefreshLayout.isRefreshing() || !empty)
                && !loadingMore);
        ViewUtils.setVisibleOrGone(mProgress, loading && empty);
        mAdapter.setProgressVisible(loading && !empty && loadingMore);
    }
}
