/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.ui;

import android.os.Bundle;

import mika.com.android.ac.user.ui.BaseUserAdapter;
import mika.com.android.ac.user.ui.UserAdapter;
import mika.com.android.ac.user.ui.UserListFragment;
import mika.com.android.ac.util.FragmentUtils;

public abstract class FollowshipListFragment extends UserListFragment {

    // Not static because we are to be subclassed.
    private final String KEY_PREFIX = getClass().getName() + '.';

    public final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";

    private String mUserIdOrUid;

    protected void setArguments(String userIdOrUid) {
        FragmentUtils.ensureArguments(this)
                .putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserIdOrUid = getArguments().getString(EXTRA_USER_ID_OR_UID);
    }

    @Override
    protected BaseUserAdapter onCreateAdapter() {
        return new UserAdapter();
    }

    protected String getUserIdOrUid() {
        return mUserIdOrUid;
    }
}
