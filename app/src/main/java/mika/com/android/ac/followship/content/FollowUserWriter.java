/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.content;

import android.content.Context;
import android.support.annotation.Keep;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import mika.com.android.ac.content.ResourceWriter;
import mika.com.android.ac.eventbus.UserInfoUpdatedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.eventbus.UserInfoWriteFinishedEvent;
import mika.com.android.ac.eventbus.UserInfoWriteStartedEvent;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;
import mika.com.android.ac.network.api.ApiContract;

class FollowUserWriter extends ResourceWriter<FollowUserWriter, UserInfo> {

    private String mUserIdOrUid;
    private UserInfo mUserInfo;
    private boolean mFollow;

    private FollowUserWriter(String userIdOrUid, UserInfo userInfo, boolean follow,
                             FollowUserManager manager) {
        super(manager);

        mUserIdOrUid = userIdOrUid;
        mUserInfo = userInfo;
        mFollow = follow;

        EventBusUtils.register(this);
    }

    FollowUserWriter(String userIdOrUid, boolean follow, FollowUserManager manager) {
        this(userIdOrUid, null, follow, manager);
    }

    FollowUserWriter(UserInfo userInfo, boolean follow, FollowUserManager manager) {
        this(userInfo.getIdOrUid(), userInfo, follow, manager);
    }

    public String getUserIdOrUid() {
        return mUserIdOrUid;
    }

    public boolean hasUserIdOrUid(String userIdOrUid) {
        return mUserInfo != null ? mUserInfo.hasIdOrUid(userIdOrUid)
                : TextUtils.equals(mUserIdOrUid, userIdOrUid);
    }

    public boolean isFollow() {
        return mFollow;
    }

    @Override
    protected Request<UserInfo> onCreateRequest() {
        return ApiRequests.newFollowshipRequest(mUserIdOrUid, mFollow);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new UserInfoWriteStartedEvent(mUserIdOrUid, this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onResponse(UserInfo response) {

        ToastUtil.show(mFollow ? mika.com.android.ac.R.string.user_follow_successful
                : mika.com.android.ac.R.string.user_unfollow_successful, getContext());

        EventBusUtils.postAsync(new UserInfoUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtil.show(context.getString(mFollow ? mika.com.android.ac.R.string.user_follow_failed_format
                        : mika.com.android.ac.R.string.user_unfollow_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mUserInfo != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeFollowed = null;
            if (apiError.code == ApiContract.Response.Error.Codes.Followship.ALREADY_FOLLOWED) {
                shouldBeFollowed = true;
            } else if (apiError.code == ApiContract.Response.Error.Codes.Followship.NOT_FOLLOWED_YET) {
                shouldBeFollowed = false;
            }
            if (shouldBeFollowed != null) {
                mUserInfo.fixFollowed(shouldBeFollowed);
                EventBusUtils.postAsync(new UserInfoUpdatedEvent(mUserInfo, this));
                notified = true;
            }
        }
        if (!notified) {
            // Must notify to reset pending status. Off-screen items also needs to be invalidated.
            EventBusUtils.postAsync(new UserInfoWriteFinishedEvent(mUserIdOrUid, this));
        }

        stopSelf();
    }

    @Keep
    public void onEventMainThread(UserInfoUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        //noinspection deprecation
        if (event.userInfo.hasIdOrUid(mUserIdOrUid)) {
            mUserIdOrUid = event.userInfo.getIdOrUid();
            mUserInfo = event.userInfo;
        }
    }
}
