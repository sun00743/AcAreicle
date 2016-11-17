/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.credential;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SetApiCredentialReceiver extends BroadcastReceiver {

    private static final String EXTRA_API_KEY = "me.zhanghai.android.douya.intent.extra.API_KEY";
    private static final String EXTRA_API_SECRET =
            "me.zhanghai.android.douya.intent.extra.API_SECRET";

    @Override
    public void onReceive(Context context, Intent intent) {
        String apiKey = intent.getStringExtra(EXTRA_API_KEY);
        String apiSecret = intent.getStringExtra(EXTRA_API_SECRET);
        ApiCredentialManager.setApiCredential(apiKey, apiSecret, context);
    }
}
