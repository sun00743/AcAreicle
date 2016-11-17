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

import mika.com.android.ac.util.ToastUtil;

public class NotImplementedManager {

    private NotImplementedManager() {}

    public static void openDoumail(Context context) {
        UrlHandler.open("https://www.douban.com/doumail/", context);
    }

    public static void openSearch(Context context) {
        UrlHandler.open("https://www.douban.com/search", context);
    }

    public static void sendBroadcast(String topic, Context context) {
        if (!FrodoBridge.sendBroadcast(topic, context)) {
            UrlHandler.open("https://www.douban.com/#isay-cont", context);
        }
    }

    public static void showNotYetImplementedToast(Context context) {
        ToastUtil.show(mika.com.android.ac.R.string.not_yet_implemented, context);
    }
}
