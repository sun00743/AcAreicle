/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.link;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import mika.com.android.ac.settings.info.Settings;
import mika.com.android.ac.util.AppUtils;
import mika.com.android.ac.util.UrlUtils;

public class UrlHandler {

    private static final CustomTabsActivityHelper.CustomTabsFallback sFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    open(uri, activity, false);
                }
            };

    private UrlHandler() {}

    private static void open(Uri uri, Context context, boolean enableCustomTabs) {

        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme)) {
            switch (uri.getScheme()) {
                case "http":
                case "https":
                case "ftp":
                    break;
                default:
                    UrlUtils.openWithIntent(uri, context);
                    return;
            }
        }

        switch (Settings.OPEN_URL_WITH_METHOD.getEnumValue(context)) {
            case CUSTOM_TABS:
                if (enableCustomTabs) {
                    Activity activity = AppUtils.getActivityFromContext(context);
                    if (activity != null) {
                        UrlUtils.openWithCustomTabs(uri, sFallback, activity);
                    }
                    break;
                }
                // Fall through!
            case WEBVIEW:
                UrlUtils.openWithWebViewActivity(uri, context);
                break;
            case INTENT:
                UrlUtils.openWithIntent(uri, context);
                break;
        }
    }

    public static void open(Uri uri, Context context) {
        open(uri, context, true);
    }

    public static void open(String url, Context context) {
        open(Uri.parse(url), context);
    }
}
