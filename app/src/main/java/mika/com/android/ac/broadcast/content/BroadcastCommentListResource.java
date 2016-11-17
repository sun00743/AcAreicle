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

import mika.com.android.ac.eventbus.BroadcastCommentSentEvent;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.CommentList;
import mika.com.android.ac.util.FragmentUtils;

public class BroadcastCommentListResource extends CommentListResource {

    private static final String KEY_PREFIX = BroadcastCommentListResource.class.getName() + '.';

    public static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";

    private long mBroadcastId;

    private static final String FRAGMENT_TAG_DEFAULT = BroadcastCommentListResource.class.getName();

    private static BroadcastCommentListResource newInstance(long broadcastId) {
        //noinspection deprecation
        BroadcastCommentListResource resource = new BroadcastCommentListResource();
        resource.setArguments(broadcastId);
        return resource;
    }

    public static BroadcastCommentListResource attachTo(long broadcastId, FragmentActivity activity,
                                                        String tag, int requestCode) {
        return attachTo(broadcastId, activity, tag, true, null, requestCode);
    }

    public static BroadcastCommentListResource attachTo(long broadcastId,
                                                        FragmentActivity activity) {
        return attachTo(broadcastId, activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static BroadcastCommentListResource attachTo(long broadcastId, Fragment fragment,
                                                        String tag, int requestCode) {
        return attachTo(broadcastId, fragment.getActivity(), tag, false, fragment, requestCode);
    }

    public static BroadcastCommentListResource attachTo(long broadcastId, Fragment fragment) {
        return attachTo(broadcastId, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    private static BroadcastCommentListResource attachTo(long broadcastId,
                                                         FragmentActivity activity, String tag,
                                                         boolean targetAtActivity,
                                                         Fragment targetFragment, int requestCode) {
        BroadcastCommentListResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(broadcastId);
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
    public BroadcastCommentListResource() {}

    protected void setArguments(long broadcastId) {
        FragmentUtils.ensureArguments(this)
                .putLong(EXTRA_BROADCAST_ID, broadcastId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcastId = getArguments().getLong(EXTRA_BROADCAST_ID);
    }

    @Override
    protected ApiRequest<CommentList> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newBroadcastCommentListRequest(mBroadcastId, start, count);
    }

    @Keep
    public void onEventMainThread(BroadcastCommentSentEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastId) {
            append(event.comment);
        }
    }
}
