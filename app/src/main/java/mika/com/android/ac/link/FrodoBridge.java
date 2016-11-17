/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.link;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class FrodoBridge {

    private static final String FRODO_SCHEME = "douban";
    private static final String FACADE_ACTION = "com.douban.frodo";

    private static final String FRODO_PACKAGE_NAME = "com.douban.frodo";

    private static final String SEND_STATUS_CLASS_NAME = ".activity.StatusEditActivity";
    private static final String SEND_STATUS_EXTRA_HASHTAG_NAME = "hashtag_name";

    private FrodoBridge() {}

    public static boolean openUri(Uri uri, Context context) {
        return startActivity(makeIntent(uri), context);
    }

    private static Intent makeIntent(Uri uri) {
        return new Intent(FACADE_ACTION, uri);
    }

    public static boolean openFrodoUri(Uri uri, Context context) {
        return isFrodoUri(uri) && openUri(uri, context);
    }

    public static boolean isFrodoUri(Uri uri) {
        return TextUtils.equals(uri.getScheme(), FRODO_SCHEME);
    }

    public static boolean sendBroadcast(String topic, Context context) {
        return startActivity(makeSendBroadcastIntent(topic), context);
    }

    private static Intent makeSendBroadcastIntent(String hashTagName) {
        return makeIntent(SEND_STATUS_CLASS_NAME)
                .putExtra(SEND_STATUS_EXTRA_HASHTAG_NAME, hashTagName);
    }

    private static Intent makeIntent(String className) {
        return new Intent()
                .setComponent(new ComponentName(FRODO_PACKAGE_NAME,
                        FRODO_PACKAGE_NAME + className));
    }

    private static boolean startActivity(Intent intent, Context context) {
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
