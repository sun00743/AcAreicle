/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.notification.app;

import android.content.Context;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import mika.com.android.ac.network.api.info.frodo.Notification;
import mika.com.android.ac.util.Callback;
import mika.com.android.ac.util.DiskCacheHelper;

public class NotificationListCache {

    private static final int MAX_LIST_SIZE = 20;

    private static final String KEY = NotificationListCache.class.getName();

    public static void get(Handler handler, Callback<List<Notification>> callback,
                           Context context) {
        DiskCacheHelper.getGson(KEY, new TypeToken<List<Notification>>() {}, handler, callback,
                context);
    }

    public static void put(List<Notification> notificationList, Context context) {
        if (notificationList.size() > MAX_LIST_SIZE) {
            notificationList = notificationList.subList(0, MAX_LIST_SIZE);
        }
        DiskCacheHelper.putGson(KEY, notificationList, new TypeToken<List<Notification>>() {},
                context);
    }
}
