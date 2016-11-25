/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.TransitionUtils;

public class BroadcastActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = BroadcastActivity.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";
    private static final String EXTRA_SHOW_SEND_COMMENT = KEY_PREFIX + "show_send_comment";
    private static final String EXTRA_TITLE = KEY_PREFIX + "title";

    public static Intent makeIntent(long broadcastId, Context context) {
        return new Intent(context, BroadcastActivity.class)
                .putExtra(EXTRA_BROADCAST_ID, broadcastId);
    }

    public static Intent makeIntent(Broadcast broadcast, Context context) {
        return new Intent(context, BroadcastActivity.class)
                .putExtra(EXTRA_BROADCAST, broadcast);
    }

    public static Intent makeIntent(Broadcast broadcast, boolean showSendComment, String title,
                                    Context context) {
        return makeIntent(broadcast, context)
                .putExtra(EXTRA_SHOW_SEND_COMMENT, showSendComment)
                .putExtra(EXTRA_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TransitionUtils.setupTransitionBeforeDecorate(this);
        super.onCreate(savedInstanceState);
        // Calls ensureSubDecor().
        findViewById(android.R.id.content);
        TransitionUtils.postponeTransition(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            long broadcastId = intent.getLongExtra(EXTRA_BROADCAST_ID, -1);
            Broadcast broadcast = intent.getParcelableExtra(EXTRA_BROADCAST);
            boolean showSendComment = intent.getBooleanExtra(EXTRA_SHOW_SEND_COMMENT, false);
            String title = intent.getStringExtra(EXTRA_TITLE);
            FragmentUtils.add(
                    BroadcastFragment.newInstance(broadcastId, broadcast, showSendComment, title),
                    this, android.R.id.content);
        }
    }
}
