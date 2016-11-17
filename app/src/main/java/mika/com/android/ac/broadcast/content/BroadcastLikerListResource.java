/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.util.FragmentUtils;

public class BroadcastLikerListResource extends BroadcastUserListResource {

    private static final String FRAGMENT_TAG_DEFAULT = BroadcastLikerListResource.class.getName();

    private static BroadcastLikerListResource newInstance(long broadcastId) {
        //noinspection deprecation
        BroadcastLikerListResource resource = new BroadcastLikerListResource();
        resource.setArguments(broadcastId);
        return resource;
    }

    public static BroadcastLikerListResource attachTo(long broadcastId, FragmentActivity activity,
                                                      String tag, int requestCode) {
        return attachTo(broadcastId, activity, tag, true, null, requestCode);
    }

    public static BroadcastLikerListResource attachTo(long broadcastId, FragmentActivity activity) {
        return attachTo(broadcastId, activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static BroadcastLikerListResource attachTo(long broadcastId, Fragment fragment,
                                                      String tag, int requestCode) {
        return attachTo(broadcastId, fragment.getActivity(), tag, false, fragment, requestCode);
    }

    public static BroadcastLikerListResource attachTo(long broadcastId, Fragment fragment) {
        return attachTo(broadcastId, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    private static BroadcastLikerListResource attachTo(long broadcastId, FragmentActivity activity,
                                                      String tag, boolean targetAtActivity,
                                                      Fragment targetFragment, int requestCode) {
        BroadcastLikerListResource resource = FragmentUtils.findByTag(activity, tag);
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
    public BroadcastLikerListResource() {}

    @Override
    protected ApiRequest<List<User>> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newBroadcastLikerListRequest(getBroadcastId(), start, count);
    }
}
