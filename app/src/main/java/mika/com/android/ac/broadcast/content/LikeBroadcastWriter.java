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
import android.support.annotation.Keep;

import com.android.volley.VolleyError;

import mika.com.android.ac.R;
import mika.com.android.ac.content.ResourceWriter;
import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteFinishedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteStartedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.ApiContract.Response.Error.Codes;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;

class LikeBroadcastWriter extends ResourceWriter<LikeBroadcastWriter, Broadcast> {

    private long mBroadcastId;
    private Broadcast mBroadcast;
    private boolean mLike;

    private LikeBroadcastWriter(long broadcastId, Broadcast broadcast, boolean like,
                                LikeBroadcastManager manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mBroadcast = broadcast;
        mLike = like;

        EventBusUtils.register(this);
    }

    LikeBroadcastWriter(long broadcastId, boolean like, LikeBroadcastManager manager) {
        this(broadcastId, null, like, manager);
    }

    LikeBroadcastWriter(Broadcast broadcast, boolean like, LikeBroadcastManager manager) {
        this(broadcast.id, broadcast, like, manager);
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public boolean isLike() {
        return mLike;
    }

    @Override
    protected Request<Broadcast> onCreateRequest() {
        return ApiRequests.newLikeBroadcastRequest(mBroadcastId, mLike);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new BroadcastWriteStartedEvent(mBroadcastId, this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onResponse(Broadcast response) {

        ToastUtil.show(mLike ? R.string.broadcast_like_successful
                : R.string.broadcast_unlike_successful, getContext());

        EventBusUtils.postAsync(new BroadcastUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtil.show(context.getString(mLike ? R.string.broadcast_like_failed_format
                        : R.string.broadcast_unlike_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mBroadcast != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeLiked = null;
            if (apiError.code == Codes.LikeBroadcast.ALREADY_LIKED) {
                shouldBeLiked = true;
            } else if (apiError.code == Codes.LikeBroadcast.NOT_LIKED_YET) {
                shouldBeLiked = false;
            }
            if (shouldBeLiked != null) {
                mBroadcast.fixLiked(shouldBeLiked);
                EventBusUtils.postAsync(new BroadcastUpdatedEvent(mBroadcast, this));
                notified = true;
            }
        }
        if (!notified) {
            // Must notify to reset pending status. Off-screen items also needs to be invalidated.
            EventBusUtils.postAsync(new BroadcastWriteFinishedEvent(mBroadcastId, this));
        }

        stopSelf();
    }

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcast.id == mBroadcast.id) {
            mBroadcast = event.broadcast;
        }
    }
}
