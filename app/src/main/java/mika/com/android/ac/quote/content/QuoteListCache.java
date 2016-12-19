package mika.com.android.ac.quote.content;

import android.content.Context;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.util.Callback;
import mika.com.android.ac.util.DiskCacheHelper;

/**
 * Created by mika on 2016/12/14.
 */

public class QuoteListCache {
    private static final int MAX_LIST_SIZE = 10;
    private static final String KEY_PREFIX = QuoteListCache.class.getName();
    private static final String NAME = "yue";
    private static final int TYPE_QQ = 0x01;
    private static final int TYPE_MY = 0x02;

    /**
     * get quote comment
     */
    public static void getQuoteQComment(int userId, Handler handler, Callback<List<Comment>> callback, Context context) {
        DiskCacheHelper.getGson(getKey(userId, TYPE_QQ), new TypeToken<List<Comment>>() {
        }, handler, callback, context);
    }

    /**
     * get my comment
     */
    public static void getQUoteMyComment(int usersId, Handler handler, Callback<List<Comment>> callback, Context context) {
        DiskCacheHelper.getGson(getKey(usersId, TYPE_MY), new TypeToken<List<Comment>>() {
        }, handler, callback, context);
    }

    /**
     * put quote comment
     */
    public static void putQuoteQComment(int userId, List<Comment> commentList, Context context) {
        if (commentList.size() > MAX_LIST_SIZE) {
            commentList = commentList.subList(0, MAX_LIST_SIZE);
        }
        DiskCacheHelper.putGson(getKey(userId, TYPE_QQ), new ArrayList<>(commentList),
                new TypeToken<List<Comment>>() {
                }, context);
    }

    /**
     * put my comment
     */
    public static void putQuoteMyComment(int userId, List<Comment> commentList, Context context) {
        if (commentList.size() > MAX_LIST_SIZE) {
            commentList = commentList.subList(0, MAX_LIST_SIZE);
        }
        DiskCacheHelper.putGson(getKey(userId, TYPE_MY), new ArrayList<>(commentList),
                new TypeToken<List<Comment>>() {
                }, context);
    }

    private static String getKey(int userId, int type) {
        String key = null;
        switch (type) {
            case TYPE_QQ:
                return KEY_PREFIX + '@' + NAME + userId + TYPE_QQ;
            case TYPE_MY:
                return KEY_PREFIX + '@' + NAME + userId + TYPE_MY;
            default:
                return null;
        }
    }
}
