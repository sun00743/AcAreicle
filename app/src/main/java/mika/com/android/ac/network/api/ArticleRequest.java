/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import mika.com.android.ac.network.api.info.acapi.Article;
import mika.com.android.ac.util.Connectivity;

/**
 * Created by Administrator on 2016/9/13.
 */

public class ArticleRequest extends BaseArticleRequest<Article> {
    private static final String TAG = "Article";
    public static final int MAX_AGE = 7 * 24 * 60 * 60 * 1000;
    private static final String ARTICLE_API =  "http://api.aixifan.com/articles/";

    public ArticleRequest(Context context, int aid, Response.Listener<Article> listener, Response.ErrorListener errListener) {
        super(ARTICLE_API + aid, Article.class, listener, errListener);
    }

    @Override
    protected Response<Article> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject rsp = JSON.parseObject(json);
            int code = rsp.getIntValue("code");
            if(code != 200){
                throw new Article.InvalidArticleError();
            }
            JSONObject articleJson = rsp.getJSONObject("data");
            if(articleJson == null)
                return Response.error(new Article.InvalidArticleError());
            return Response.success(Article.newArticle(articleJson),
                    Connectivity.newCache(response, MAX_AGE));
        } catch(Article.InvalidArticleError e){
            Log.w(TAG, "Invalid Article! Need to redirect intent");
            return Response.error(e);
        } catch (Exception e) {
            Log.e(TAG, "parse article error", e);
            return Response.error(new ParseError(e));
        }
    }
}
