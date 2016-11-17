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

import mika.com.android.ac.content.ResourceWriterManager;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.util.ToastUtil;

public class FollowUserManager extends ResourceWriterManager<FollowUserWriter> {

    private static class InstanceHolder {
        public static final FollowUserManager VALUE = new FollowUserManager();
    }

    public static FollowUserManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(UserInfo, boolean, Context)} instead.
     */
    public void write(String userIdOrUid, boolean like, Context context) {
        add(new FollowUserWriter(userIdOrUid, like, this), context);
    }

    public boolean write(UserInfo userInfo, boolean like, Context context) {
        if (shouldWrite(userInfo, context)) {
            add(new FollowUserWriter(userInfo, like, this), context);
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldWrite(UserInfo userInfo, Context context) {
        if (userInfo.isOneself(context)) {
            ToastUtil.show(mika.com.android.ac.R.string.user_follow_error_cannot_follow_oneself, context);
            return false;
        } else {
            return true;
        }
    }

    public boolean isWriting(String userIdOrUid) {
        return findWriter(userIdOrUid) != null;
    }

    public boolean isWritingFollow(String userIdOrUid) {
        FollowUserWriter writer = findWriter(userIdOrUid);
        return writer != null && writer.isFollow();
    }

    private FollowUserWriter findWriter(String userIdOrUid) {
        for (FollowUserWriter writer : getWriters()) {
            if (writer.hasUserIdOrUid(userIdOrUid)) {
                return writer;
            }
        }
        return null;
    }
}
