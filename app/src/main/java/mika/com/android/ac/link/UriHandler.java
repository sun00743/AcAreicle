/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.link;

import android.content.Context;
import android.net.Uri;

public class UriHandler {

    private UriHandler() {}

    public static void open(Uri uri, Context context) {
        if (DoubanUriHandler.open(uri, context)) {
            return;
        }
        if (FrodoBridge.openFrodoUri(uri, context)) {
            return;
        }
        UrlHandler.open(uri, context);
    }

    public static void open(String url, Context context) {
        open(Uri.parse(url), context);
    }
}
