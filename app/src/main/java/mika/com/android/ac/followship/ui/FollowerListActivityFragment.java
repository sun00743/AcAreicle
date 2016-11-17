/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.ui;

public class FollowerListActivityFragment extends FollowshipListActivityFragment {

    /**
     * @deprecated Use {@link #newInstance(String)} instead.
     */
    public FollowerListActivityFragment() {}

    public static FollowerListActivityFragment newInstance(String userIdOrUid) {
        //noinspection deprecation
        FollowerListActivityFragment fragment = new FollowerListActivityFragment();
        fragment.setArguments(userIdOrUid);
        return fragment;
    }

    @Override
    protected FollowshipListFragment onCreateListFragment() {
        return FollowerListFragment.newInstance(getUserIdOrUid());
    }
}
