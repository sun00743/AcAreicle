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

import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.util.FragmentUtils;

public class ProfileActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = ProfileFragment.class.getName() + '.';

    private static final String EXTRA_ACER = KEY_PREFIX + "acer";
    private static final String EXTRA_ACER_INFO = KEY_PREFIX + "acer_info";

    private ProfileFragment mProfileFragment;

    public static Intent makeIntent(Acer acer, AcerInfo2 acerInfo, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_ACER_INFO, acerInfo)
                .putExtra(EXTRA_ACER, acer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Acer acer = intent.getParcelableExtra(EXTRA_ACER);
            AcerInfo2 acerInfo = intent.getParcelableExtra(EXTRA_ACER_INFO);
            mProfileFragment = ProfileFragment.newInstance(acer, acerInfo);
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
