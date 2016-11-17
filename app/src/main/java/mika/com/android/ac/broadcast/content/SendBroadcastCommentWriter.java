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

import mika.com.android.ac.R;
import mika.com.android.ac.content.ResourceWriter;
import mika.com.android.ac.eventbus.BroadcastCommentSendErrorEvent;
import mika.com.android.ac.eventbus.BroadcastCommentSentEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;

class SendBroadcastCommentWriter extends ResourceWriter<SendBroadcastCommentWriter, Comment> {

    private long mBroadcastId;
    private String  mComment;

    SendBroadcastCommentWriter(long broadcastId, String comment,
                               SendBroadcastCommentManager manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mComment = comment;
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public String getComment() {
        return mComment;
    }

    @Override
    protected Request<Comment> onCreateRequest() {
        return ApiRequests.newSendBroadcastCommentRequest(mBroadcastId, mComment);
    }

    @Override
    public void onResponse(Comment response) {

        ToastUtil.show(R.string.broadcast_send_comment_successful, getContext());

        EventBusUtils.postAsync(new BroadcastCommentSentEvent(mBroadcastId, response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtil.show(context.getString(R.string.broadcast_send_comment_failed_format,
                ApiError.getErrorString(error, context)), context);

        EventBusUtils.postAsync(new BroadcastCommentSendErrorEvent(mBroadcastId, this));

        stopSelf();
    }
}
