/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.UserList;
import mika.com.android.ac.util.FragmentUtils;

public class FollowingListResource extends FollowshipUserListResource {

    private static final String FRAGMENT_TAG_DEFAULT = FollowingListResource.class.getName();

    private static FollowingListResource newInstance(String userIdOrUid) {
        //noinspection deprecation
        FollowingListResource resource = new FollowingListResource();
        resource.setArguments(userIdOrUid);
        return resource;
    }

    public static FollowingListResource attachTo(String userIdOrUid, FragmentActivity activity,
                                                 String tag, int requestCode) {
        return attachTo(userIdOrUid, activity, tag, true, null, requestCode);
    }

    public static FollowingListResource attachTo(String userIdOrUid, FragmentActivity activity) {
        return attachTo(userIdOrUid, activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static FollowingListResource attachTo(String userIdOrUid, Fragment fragment,
                                                 String tag, int requestCode) {
        return attachTo(userIdOrUid, fragment.getActivity(), tag, false, fragment, requestCode);
    }

    public static FollowingListResource attachTo(String userIdOrUid, Fragment fragment) {
        return attachTo(userIdOrUid, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    private static FollowingListResource attachTo(String userIdOrUid, FragmentActivity activity,
                                                  String tag, boolean targetAtActivity,
                                                  Fragment targetFragment, int requestCode) {
        FollowingListResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(userIdOrUid);
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
    public FollowingListResource() {}

    @Override
    protected ApiRequest<UserList> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newFollowingListRequest(getUserIdOrUid(), start, count);
    }
}
