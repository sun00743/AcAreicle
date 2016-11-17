/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import mika.com.android.ac.ui.WebViewActivity;

public class UrlUtils {

    private UrlUtils() {}

    public static void openWithWebViewActivity(Uri uri, Context context) {
        context.startActivity(WebViewActivity.makeIntent(uri, context));
    }

    public static void openWithIntent(Uri uri, Context context) {
        Intent intent = IntentUtils.makeView(uri);
        AppUtils.startActivity(intent, context);
    }

    public static void openWithIntent(String url, Context context) {
        openWithIntent(Uri.parse(url), context);
    }

    public static void openWithCustomTabs(Uri uri,
                                          CustomTabsActivityHelper.CustomTabsFallback fallback,
                                          Activity activity) {
        CustomTabsHelperFragment.open(activity, makeCustomTabsIntent(activity), uri, fallback);
    }

    private static CustomTabsIntent makeCustomTabsIntent(Context context) {
        return new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
                .setToolbarColor(ViewUtils.getColorFromAttrRes(mika.com.android.ac.R.attr.colorPrimary, 0, context))
                .setShowTitle(true)
                .build();
    }
}
