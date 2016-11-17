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
import android.support.v7.app.AppCompatActivity;

import mika.com.android.ac.util.FragmentUtils;

public abstract class FollowshipListActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = FollowshipListActivity.class.getName() + '.';

    protected static final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            String userIdOrUid = getIntent().getStringExtra(EXTRA_USER_ID_OR_UID);
            FragmentUtils.add(onCreateActivityFragment(userIdOrUid), this, android.R.id.content);
        }
    }

    abstract protected FollowshipListActivityFragment onCreateActivityFragment(String userIdOrUid);
}
