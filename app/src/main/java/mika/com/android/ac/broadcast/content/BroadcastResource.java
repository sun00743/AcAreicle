/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.android.volley.VolleyError;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.eventbus.BroadcastDeletedEvent;
import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteFinishedEvent;
import mika.com.android.ac.eventbus.BroadcastWriteStartedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.RequestFragment;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.app.TargetedRetainedFragment;

public class BroadcastResource extends ResourceFragment
        implements RequestFragment.Listener<Broadcast, Void> {

    private static final String KEY_PREFIX = BroadcastResource.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";

    private static final int BROADCAST_ID_INVALID = -1;

    private long mBroadcastId = BROADCAST_ID_INVALID;

    private Broadcast mBroadcast;

    private boolean mLoading;

    private static final String FRAGMENT_TAG_DEFAULT = BroadcastResource.class.getName();

    private static BroadcastResource newInstance(long broadcastId, Broadcast broadcast) {
        //noinspection deprecation
        BroadcastResource resource = new BroadcastResource();
        resource.setArguments(broadcastId, broadcast);
        return resource;
    }

    public static BroadcastResource attachTo(long broadcastId, Broadcast broadcast,
                                             FragmentActivity activity, String tag,
                                             int requestCode) {
        return attachTo(broadcastId, broadcast, activity, tag, true, null, requestCode);
    }

    public static BroadcastResource attachTo(long broadcastId, Broadcast broadcast,
                                             FragmentActivity activity) {
        return attachTo(broadcastId, broadcast, activity, FRAGMENT_TAG_DEFAULT,
                TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    public static BroadcastResource attachTo(long broadcastId, Broadcast broadcast,
                                             Fragment fragment, String tag, int requestCode) {
        return attachTo(broadcastId, broadcast, fragment.getActivity(), tag, false, fragment,
                requestCode);
    }

    public static BroadcastResource attachTo(long broadcastId, Broadcast broadcast,
                                             Fragment fragment) {
        return attachTo(broadcastId, broadcast, fragment, FRAGMENT_TAG_DEFAULT,
                TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    private static BroadcastResource attachTo(long broadcastId, Broadcast broadcast,
                                              FragmentActivity activity, String tag,
                                              boolean targetAtActivity, Fragment targetFragment,
                                              int requestCode) {
        BroadcastResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(broadcastId, broadcast);
            if (targetAtActivity) {
                resource.targetAtActivity(requestCode);
            } else {
                resource.targetAtFragment(targetFragment, requestCode);
            }
            FragmentUtils.add(resource, activity, tag);
        }
        return resource;
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public BroadcastResource() {}

    protected void setArguments(long broadcastId, Broadcast broadcast) {
        Bundle arguments = FragmentUtils.ensureArguments(this);
        arguments.putLong(EXTRA_BROADCAST_ID, broadcastId);
        arguments.putParcelable(EXTRA_BROADCAST, broadcast);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ensureBroadcastAndIdFromArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getArguments().putParcelable(EXTRA_BROADCAST, mBroadcast);
    }

    public long getBroadcastId() {
        ensureBroadcastAndIdFromArguments();
        return mBroadcastId;
    }

    public Broadcast get() {
        // Can be called before onCreate() is called.
        ensureBroadcastAndIdFromArguments();
        return mBroadcast;
    }

    public boolean isEmpty() {
        // Can be called before onCreate() is called.
        ensureBroadcastAndIdFromArguments();
        return mBroadcast == null;
    }

    public boolean isLoading() {
        return mLoading;
    }

    private void ensureBroadcastAndIdFromArguments() {
        if (mBroadcastId == BROADCAST_ID_INVALID) {
            Bundle arguments = getArguments();
            mBroadcast = arguments.getParcelable(EXTRA_BROADCAST);
            if (mBroadcast != null) {
                mBroadcastId = mBroadcast.id;
            } else {
                mBroadcastId = arguments.getLong(EXTRA_BROADCAST_ID);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.register(this);

        if (mBroadcast == null) {
            load();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBusUtils.unregister(this);
    }

    public void load() {

        if (mLoading) {
            return;
        }

        mLoading = true;
        getListener().onLoadBroadcastStarted(getRequestCode());

        ApiRequest<Broadcast> request = ApiRequests.newBroadcastRequest(mBroadcastId);
        RequestFragment.startRequest(request, null, this);
    }

    @Override
    public void onVolleyResponse(int requestCode, final boolean successful,
                                 final Broadcast result, final VolleyError error,
                                 final Void requestState) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                onLoadFinished(successful, result, error);
            }
        });
    }

    private void onLoadFinished(boolean successful, Broadcast broadcast, VolleyError error) {

        mLoading = false;
        getListener().onLoadBroadcastFinished(getRequestCode());

        if (successful) {
            mBroadcast = broadcast;
            getListener().onBroadcastChanged(getRequestCode(), mBroadcast);
            EventBusUtils.postAsync(new BroadcastUpdatedEvent(mBroadcast, this));
        } else {
            getListener().onLoadBroadcastError(getRequestCode(), error);
        }
    }

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        boolean changed = false;
        if (event.broadcast.id == mBroadcastId) {
            mBroadcast = event.broadcast;
            changed = true;
        } else if (mBroadcast != null && mBroadcast.rebroadcastedBroadcast != null
                && event.broadcast.id == mBroadcast.rebroadcastedBroadcast.id) {
            mBroadcast.rebroadcastedBroadcast = event.broadcast;
            changed = true;
        }

        if (changed) {
            getListener().onBroadcastChanged(getRequestCode(), mBroadcast);
        }
    }

    @Keep
    public void onEventMainThread(BroadcastDeletedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastId) {
            mBroadcast = null;
            getListener().onBroadcastRemoved(getRequestCode());
        }
    }

    @Keep
    public void onEventMainThread(BroadcastWriteStartedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        // Only call listener when we have the data.
        if (event.broadcastId == mBroadcastId && mBroadcast != null) {
            getListener().onBroadcastWriteStarted(getRequestCode());
        }
    }

    @Keep
    public void onEventMainThread(BroadcastWriteFinishedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        // Only call listener when we have the data.
        if (event.broadcastId == mBroadcastId && mBroadcast != null) {
            getListener().onBroadcastWriteFinished(getRequestCode());
        }
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    public interface Listener {
        void onLoadBroadcastStarted(int requestCode);
        void onLoadBroadcastFinished(int requestCode);
        void onLoadBroadcastError(int requestCode, VolleyError error);
        void onBroadcastChanged(int requestCode, Broadcast newBroadcast);
        void onBroadcastRemoved(int requestCode);
        void onBroadcastWriteStarted(int requestCode);
        void onBroadcastWriteFinished(int requestCode);
    }
}
