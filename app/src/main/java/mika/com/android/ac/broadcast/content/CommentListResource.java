/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.support.annotation.Keep;

import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.List;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.eventbus.CommentDeletedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.RequestFragment;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.network.api.info.apiv2.CommentList;

public abstract class CommentListResource extends ResourceFragment
        implements RequestFragment.Listener<CommentList, CommentListResource.State> {

    private static final int DEFAULT_COUNT_PER_LOAD = 20;

    private List<Comment> mCommentList;

    private boolean mCanLoadMore = true;
    private boolean mLoading;
    private boolean mLoadingMore;

    /**
     * @return Unmodifiable comment list, or {@code null}.
     */
    public List<Comment> get() {
        return mCommentList != null ? Collections.unmodifiableList(mCommentList) : null;
    }

    public boolean has() {
        return mCommentList != null;
    }

    public boolean isEmpty() {
        return mCommentList == null || mCommentList.isEmpty();
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

        EventBusUtils.register(this);

        if (mCommentList == null || (mCommentList.isEmpty() && mCanLoadMore)) {
            load(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBusUtils.unregister(this);
    }

    public void load(boolean loadMore, int count) {

        if (mLoading || (loadMore && !mCanLoadMore)) {
            return;
        }

        mLoading = true;
        mLoadingMore = loadMore;
        getListener().onLoadCommentListStarted(getRequestCode());

        Integer start = loadMore && mCommentList != null ? mCommentList.size() : null;
        ApiRequest<CommentList> request = onCreateRequest(start, count);
        State state = new State(loadMore, count);
        RequestFragment.startRequest(request, state, this);
    }

    public void load(boolean loadMore) {
        load(loadMore, DEFAULT_COUNT_PER_LOAD);
    }

    protected abstract ApiRequest<CommentList> onCreateRequest(Integer start, Integer count);

    @Override
    public void onVolleyResponse(int requestCode, final boolean successful,
                                 final CommentList result, final VolleyError error,
                                 final State requestState) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                onLoadFinished(successful, result != null ? result.comments : null, error,
                        requestState.loadMore, requestState.count);
            }
        });
    }

    private void onLoadFinished(boolean successful, List<Comment> commentList,
                                VolleyError error, boolean loadMore, int count) {

        mLoading = false;
        mLoadingMore = false;
        getListener().onLoadCommentListFinished(getRequestCode());

        if (successful) {
            mCanLoadMore = commentList.size() == count;
            if (loadMore) {
                mCommentList.addAll(commentList);
                getListener().onCommentListAppended(getRequestCode(),
                        Collections.unmodifiableList(commentList));
            } else {
                mCommentList = commentList;
                getListener().onCommentListChanged(getRequestCode(),
                        Collections.unmodifiableList(commentList));
            }
        } else {
            getListener().onLoadCommentListError(getRequestCode(), error);
        }
    }

    protected void append(Comment comment) {
        mCommentList.add(comment);
        getListener().onCommentListAppended(getRequestCode(), Collections.singletonList(comment));
    }

    @Keep
    public void onEventMainThread(CommentDeletedEvent event) {

        if (event.isFromMyself(this) || mCommentList == null) {
            return;
        }

        for (int i = 0, size = mCommentList.size(); i < size; ) {
            Comment comment = mCommentList.get(i);
            if (comment.id == event.commentId) {
                mCommentList.remove(i);
                getListener().onCommentRemoved(getRequestCode(), i);
                --size;
            } else {
                ++i;
            }
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
        void onLoadCommentListStarted(int requestCode);
        void onLoadCommentListFinished(int requestCode);
        void onLoadCommentListError(int requestCode, VolleyError error);
        /**
         * @param newCommentList Unmodifiable.
         */
        void onCommentListChanged(int requestCode, List<Comment> newCommentList);
        /**
         * @param appendedCommentList Unmodifiable.
         */
        void onCommentListAppended(int requestCode, List<Comment> appendedCommentList);
        void onCommentRemoved(int requestCode, int position);
    }
}
