/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.Callback;
import mika.com.android.ac.util.DiskCacheHelper;

public class HomeBroadcastListCache {

    private static final int MAX_LIST_SIZE = 20;

    private static final String KEY_PREFIX = HomeBroadcastListCache.class.getName();

    public static void get(Account account, Handler handler, Callback<List<Broadcast>> callback,
                           Context context) {
        DiskCacheHelper.getGson(getKeyForAccount(account), new TypeToken<List<Broadcast>>() {},
                handler, callback, context);
    }

    public static void put(Account account, List<Broadcast> broadcastList, Context context) {
        if (broadcastList.size() > MAX_LIST_SIZE) {
            broadcastList = broadcastList.subList(0, MAX_LIST_SIZE);
        }
        // NOTE: Defend against ConcurrentModificationException.
        DiskCacheHelper.putGson(getKeyForAccount(account), new ArrayList<>(broadcastList),
                new TypeToken<List<Broadcast>>() {}, context);
    }

    private static String getKeyForAccount(Account account) {
        return KEY_PREFIX + '@' + "tourist";
    }
}
