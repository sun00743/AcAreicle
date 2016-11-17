/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.ui;

import mika.com.android.ac.followship.content.FollowerListResource;
import mika.com.android.ac.user.content.BaseUserListResource;

public class FollowerListFragment extends FollowshipListFragment {

    /**
     * @deprecated Use {@link #newInstance(String)} instead.
     */
    public FollowerListFragment() {}

    public static FollowerListFragment newInstance(String userIdOrUid) {
        //noinspection deprecation
        FollowerListFragment fragment = new FollowerListFragment();
        fragment.setArguments(userIdOrUid);
        return fragment;
    }

    @Override
    protected BaseUserListResource onAttachUserListResource() {
        return FollowerListResource.attachTo(getUserIdOrUid(), this);
    }
}
