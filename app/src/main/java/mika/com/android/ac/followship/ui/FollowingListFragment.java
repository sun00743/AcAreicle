/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.ui;

import mika.com.android.ac.followship.content.FollowingListResource;
import mika.com.android.ac.user.content.BaseUserListResource;

public class FollowingListFragment extends FollowshipListFragment {

    /**
     * @deprecated Use {@link #newInstance(String)} instead.
     */
    public FollowingListFragment() {}

    public static FollowingListFragment newInstance(String userIdOrUid) {
        //noinspection deprecation
        FollowingListFragment fragment = new FollowingListFragment();
        fragment.setArguments(userIdOrUid);
        return fragment;
    }

    @Override
    protected BaseUserListResource onAttachUserListResource() {
        return FollowingListResource.attachTo(getUserIdOrUid(), this);
    }
}
