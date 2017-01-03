/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.articlelist.content;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import mika.com.android.ac.network.api.info.acapi.ArticleList;
import mika.com.android.ac.util.Callback;
import mika.com.android.ac.util.DiskCacheHelper;

public class HomeArticleDesListCache {

    private static final int MAX_LIST_SIZE = 20;

    private static final String KEY_PREFIX = HomeArticleDesListCache.class.getName();
    private static final String NAME = "yue";

    public static void get(Account account, int channelId, Handler handler, Callback<List<ArticleList>> callback,
                           Context context) {
        DiskCacheHelper.getGson(getKeyForAccount(account) + channelId, new TypeToken<List<ArticleList>>() {},
                handler, callback, context);
    }

    public static void put(Account account, int channelId, List<ArticleList> articleLists, Context context) {
        if (articleLists.size() > MAX_LIST_SIZE) {
            articleLists = articleLists.subList(0, MAX_LIST_SIZE);
        }
        // NOTE: Defend against ConcurrentModificationException.
        DiskCacheHelper.putGson(getKeyForAccount(account) + channelId, new ArrayList<>(articleLists),
                new TypeToken<List<ArticleList>>() {}, context);
    }

    public static void close(Context context){
        DiskCacheHelper.close(context);
    }

    private static String getKeyForAccount(Account account) {
//        return KEY_PREFIX + '@' + account.name;
        return KEY_PREFIX + '@' + NAME;
    }
}
