/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.articlelist.content;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.douya.network.api.info.acapi.ArticleList;
import me.zhanghai.android.douya.util.Callback;
import me.zhanghai.android.douya.util.DiskCacheHelper;

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

    private static String getKeyForAccount(Account account) {
//        return KEY_PREFIX + '@' + account.name;
        return KEY_PREFIX + '@' + NAME;
    }
}
