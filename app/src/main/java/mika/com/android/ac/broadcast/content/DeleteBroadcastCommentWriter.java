/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.content.Context;

import com.android.volley.VolleyError;

import mika.com.android.ac.content.ResourceWriter;
import mika.com.android.ac.eventbus.CommentDeletedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;

class DeleteBroadcastCommentWriter extends ResourceWriter<DeleteBroadcastCommentWriter, Boolean> {

    private long mBroadcastId;
    private long mCommentId;

    DeleteBroadcastCommentWriter(long broadcastId, long commentId,
                                 DeleteBroadcastCommentManager manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mCommentId = commentId;
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public long getCommentId() {
        return mCommentId;
    }

    @Override
    protected Request<Boolean> onCreateRequest() {
        return ApiRequests.newDeleteBroadcastCommentRequest(mBroadcastId, mCommentId);
    }

    @Override
    public void onResponse(Boolean response) {

        ToastUtil.show(mika.com.android.ac.R.string.broadcast_comment_delete_successful, getContext());

        EventBusUtils.postAsync(new CommentDeletedEvent(mCommentId, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtil.show(context.getString(mika.com.android.ac.R.string.broadcast_comment_delete_failed_format,
                ApiError.getErrorString(error, context)), context);

        stopSelf();
    }
}
