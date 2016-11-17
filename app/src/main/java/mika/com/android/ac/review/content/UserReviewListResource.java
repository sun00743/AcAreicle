/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.review.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.frodo.ReviewList;
import mika.com.android.ac.util.FragmentUtils;

public class UserReviewListResource extends BaseReviewListResource {

    private static final int DEFAULT_COUNT_PER_LOAD = 20;

    private static final String KEY_PREFIX = UserReviewListResource.class.getName() + '.';

    public final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";

    private String mUserIdOrUid;

    private static final String FRAGMENT_TAG_DEFAULT = UserReviewListResource.class.getName();

    private static UserReviewListResource newInstance(String userIdOrUid) {
        //noinspection deprecation
        UserReviewListResource resource = new UserReviewListResource();
        resource.setArguments(userIdOrUid);
        return resource;
    }

    public static UserReviewListResource attachTo(String userIdOrUid, FragmentActivity activity,
                                                  String tag, int requestCode) {
        return attachTo(userIdOrUid, activity, tag, true, null, requestCode);
    }

    public static UserReviewListResource attachTo(String userIdOrUid, FragmentActivity activity) {
        return attachTo(userIdOrUid, activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static UserReviewListResource attachTo(String userIdOrUid, Fragment fragment, String tag,
                                                  int requestCode) {
        return attachTo(userIdOrUid, fragment.getActivity(), tag, false, fragment, requestCode);
    }

    public static UserReviewListResource attachTo(String userIdOrUid, Fragment fragment) {
        return attachTo(userIdOrUid, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    private static UserReviewListResource attachTo(String userIdOrUid, FragmentActivity activity,
                                                   String tag, boolean targetAtActivity,
                                                   Fragment targetFragment, int requestCode) {
        UserReviewListResource resource = FragmentUtils.findByTag(activity, tag);
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
    public UserReviewListResource() {}

    private void setArguments(String userIdOrUid) {
        FragmentUtils.ensureArguments(this)
                .putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserIdOrUid = getArguments().getString(EXTRA_USER_ID_OR_UID);
    }

    @Override
    protected ApiRequest<ReviewList> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newUserReviewListRequest(mUserIdOrUid, start, count);
    }
}
