/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.util.FragmentUtils;

public class ProfileActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = ProfileFragment.class.getName() + '.';

    private static final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    private static final String EXTRA_USER = KEY_PREFIX + "user";
    private static final String EXTRA_USER_INFO = KEY_PREFIX + "user_info";

    private ProfileFragment mProfileFragment;

    public static Intent makeIntent(String userIdOrUid, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_USER_ID_OR_UID, userIdOrUid);
    }

    public static Intent makeIntent(User user, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_USER, user);
    }

    public static Intent makeIntent(UserInfo userInfo, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_USER_INFO, userInfo);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String userIdOrUid = intent.getStringExtra(EXTRA_USER_ID_OR_UID);
            User user = intent.getParcelableExtra(EXTRA_USER);
            UserInfo userInfo = intent.getParcelableExtra(EXTRA_USER_INFO);
            mProfileFragment = ProfileFragment.newInstance(userIdOrUid, user, userInfo);
            FragmentUtils.add(mProfileFragment, this, android.R.id.content);
        } else {
            mProfileFragment = FragmentUtils.findById(this, android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        mProfileFragment.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }
}
