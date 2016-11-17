/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.followship.ui;

import android.content.Context;
import android.content.Intent;

public class FollowerListActivity extends FollowshipListActivity {

    public static Intent makeIntent(String userIdOrUid, Context context) {
        return new Intent(context, FollowerListActivity.class)
                .putExtra(EXTRA_USER_ID_OR_UID, userIdOrUid);
    }

    @Override
    protected FollowshipListActivityFragment onCreateActivityFragment(String userIdOrUid) {
        return FollowerListActivityFragment.newInstance(userIdOrUid);
    }
}
