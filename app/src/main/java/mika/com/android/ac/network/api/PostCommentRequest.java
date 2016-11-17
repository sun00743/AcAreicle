/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import mika.com.android.ac.network.Request;
import mika.com.android.ac.util.Connectivity;

/**
 * Created by Administrator on 2016/9/30.
 */

public class PostCommentRequest extends Request {

    private static final String POST_COMMENT_URL = "http://mobile.acfun.tv/comment.aspx";
    private static final String APP_VERSION = "4.3.0";


    public PostCommentRequest() {
        super(Method.POST, POST_COMMENT_URL);
    }

    public PostCommentRequest(String text, int quotedId, String token, int userId, int contentId){
        this();
        addParam("text", text);
        addParam("quoteId", Integer.toString(quotedId));
        addParam("contentId", Integer.toString(contentId));
        addParam("source", "mobile");
        addParam("access_token", token);
        addParam("userId", Integer.toString(userId));
        addParam("captcha", "");

        addHeader("Cookie", "JSESSIONID=" + token);
        addHeader("appVersion", APP_VERSION);
        addHeader("token", token);
        addHeader("User-Agent", Connectivity.UA);
        addHeader("deviceType", "1");
        addHeader("uid", "623674");
        addHeader("Content-Type",Connectivity.CONTENT_TYPE_FORM);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> headers = response.headers;
            String cookies = headers.get("Set-Cookie");
            String data = new String(response.data, "UTF-8");
            // 解析服务器返回的cookie值
//            String cookie = parseVolleyCookie(cookies);
            // 存储cookie
//            SharedPreferenceUtil.putString(LoginActivity.this,"cookie", cookie);
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Request addHeader(String name, String value) {
        return super.addHeader(name, value);
    }

    @Override
    public Request addHeader(Map.Entry header) {
        return super.addHeader(header);
    }

    @Override
    public Request addParam(String name, String value) {
        return super.addParam(name, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return super.getHeaders();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
