/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.user.content;

import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.List;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.network.RequestFragment;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.info.apiv2.User;

public abstract class BaseUserListResource<T> extends ResourceFragment
        implements RequestFragment.Listener<T, BaseUserListResource.State> {

    private static final int DEFAULT_COUNT_PER_LOAD = 20;

    private List<User> mUserList;

    private boolean mCanLoadMore = true;
    private boolean mLoading;
    private boolean mLoadingMore;

    /**
     * @return Unmodifiable user list, or {@code null}.
     */
    public List<User> get() {
        return mUserList != null ? Collections.unmodifiableList(mUserList) : null;
    }

    public boolean has() {
        return mUserList != null;
    }

    public boolean isEmpty() {
        return mUserList == null || mUserList.isEmpty();
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mUserList == null || (mUserList.isEmpty() && mCanLoadMore)) {
            load(false);
        }
    }

    public void load(boolean loadMore, int count) {

        if (mLoading || (loadMore && !mCanLoadMore)) {
            return;
        }

        mLoading = true;
        mLoadingMore = loadMore;
        getListener().onLoadUserListStarted(getRequestCode(), loadMore);

        Integer start = loadMore ? (mUserList != null ? mUserList.size() : 0) : null;
        ApiRequest<T> request = onCreateRequest(start, count);
        State state = new State(loadMore, count);
        RequestFragment.startRequest(request, state, this);
    }

    public void load(boolean loadMore) {
        load(loadMore, DEFAULT_COUNT_PER_LOAD);
    }

    protected abstract ApiRequest<T> onCreateRequest(Integer start, Integer count);

    @Override
    public void onVolleyResponse(int requestCode, final boolean successful, final T result,
                                 final VolleyError error, final State requestState) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                onDeliverLoadFinished(successful, result, error, requestState.loadMore,
                        requestState.count);
            }
        });
    }

    protected abstract void onDeliverLoadFinished(boolean successful, T userList, VolleyError error,
                                                  boolean loadMore, int count);

    protected void onLoadFinished(boolean successful, List<User> userList, VolleyError error,
                                  boolean loadMore, int count) {

        mLoading = false;
        mLoadingMore = false;
        getListener().onLoadUserListFinished(getRequestCode(), loadMore);

        if (successful) {
            mCanLoadMore = userList.size() == count;
            if (loadMore) {
                mUserList.addAll(userList);
                getListener().onUserListAppended(getRequestCode(),
                        Collections.unmodifiableList(userList));
            } else {
                mUserList = userList;
                getListener().onUserListChanged(getRequestCode(),
                        Collections.unmodifiableList(userList));
            }
        } else {
            getListener().onLoadUserListError(getRequestCode(), error);
        }
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    static class State {

        public boolean loadMore;
        public int count;

        public State(boolean loadMore, int count) {
            this.loadMore = loadMore;
            this.count = count;
        }
    }

    public interface Listener {
        void onLoadUserListStarted(int requestCode, boolean loadMore);
        void onLoadUserListFinished(int requestCode, boolean loadMore);
        void onLoadUserListError(int requestCode, VolleyError error);
        /**
         * @param newUserList Unmodifiable.
         */
        void onUserListChanged(int requestCode, List<User> newUserList);
        /**
         * @param appendedUserList Unmodifiable.
         */
        void onUserListAppended(int requestCode, List<User> appendedUserList);
    }
}
