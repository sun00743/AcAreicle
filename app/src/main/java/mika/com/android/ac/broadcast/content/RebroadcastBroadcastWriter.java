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

import mika.com.android.ac.content.ResourceWriter;
import mika.com.android.ac.content.ResourceWriterManager;
import mika.com.android.ac.eventbus.BroadcastDeletedEvent;
import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteFinishedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteStartedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;
import mika.com.android.ac.network.api.ApiContract;

class RebroadcastBroadcastWriter extends ResourceWriter<RebroadcastBroadcastWriter, Broadcast> {

    private long mBroadcastId;
    private Broadcast mBroadcast;
    private boolean mRebroadcast;

    private RebroadcastBroadcastWriter(long broadcastId, Broadcast broadcast, boolean rebroadcast,
                                       ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mBroadcast = broadcast;
        mRebroadcast = rebroadcast;

        EventBusUtils.register(this);
    }

    RebroadcastBroadcastWriter(long broadcastId, boolean rebroadcast,
                               ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        this(broadcastId, null, rebroadcast, manager);
    }

    RebroadcastBroadcastWriter(Broadcast broadcast, boolean rebroadcast,
                               ResourceWriterManager<RebroadcastBroadcastWriter> manager) {
        this(broadcast.id, broadcast, rebroadcast, manager);
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public boolean isRebroadcast() {
        return mRebroadcast;
    }

    @Override
    protected Request<Broadcast> onCreateRequest() {
        return ApiRequests.newRebroadcastBroadcastRequest(mBroadcastId, mRebroadcast);
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

        ToastUtil.show(mRebroadcast ? mika.com.android.ac.R.string.broadcast_rebroadcast_successful
                : mika.com.android.ac.R.string.broadcast_unrebroadcast_successful, getContext());

        if (!mRebroadcast) {
            // Delete the rebroadcast broadcast by user. Must be done before we
            // update the broadcast so that we can retrieve rebroadcastId for the
            // old one.
            if (mBroadcast != null && mBroadcast.rebroadcastId != null) {
                EventBusUtils.postAsync(new BroadcastDeletedEvent(mBroadcast.rebroadcastId, this));
            }
        }

        EventBusUtils.postAsync(new BroadcastUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtil.show(context.getString(mRebroadcast ? mika.com.android.ac.R.string.broadcast_rebroadcast_failed_format
                        : mika.com.android.ac.R.string.broadcast_unrebroadcast_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mBroadcast != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeRebroadcasted = null;
            if (apiError.code == ApiContract.Response.Error.Codes.RebroadcastBroadcast.ALREADY_REBROADCASTED) {
                shouldBeRebroadcasted = true;
            } else if (apiError.code == ApiContract.Response.Error.Codes.RebroadcastBroadcast.NOT_REBROADCASTED_YET) {
                shouldBeRebroadcasted = false;
            }
            if (shouldBeRebroadcasted != null) {
                mBroadcast.fixRebroadcasted(shouldBeRebroadcasted);
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
