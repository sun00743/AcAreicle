/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.link;

import android.content.Context;

import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.util.ToastUtil;

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
        ToastUtil.show(R.string.not_yet_implemented, context);
    }
}
