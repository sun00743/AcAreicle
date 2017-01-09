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
 * Created by Administrator on 2016/9/25
 */

public class LoginRequest extends Request {

    private static final String LOGIN_URL = "http://mobile.acfun.tv/oauth2/authorize2.aspx";

    public LoginRequest() {
        super(Method.POST, LOGIN_URL);
    }

    public LoginRequest(String username, String password){
        this();
        addParam("username", username);
        addParam("password", password);

        addParam("response_type", "token");
        addParam("client_id", "ELSH6ruK0qva88DD");
        addHeader("User-Agent", Connectivity.UA);
        addHeader("deviceType", "1");
//        addHeader("Content-Type",Connectivity.CONTENT_TYPE_FORM);
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
            // 下面这句是把返回的数据传到onResponse回调里,一定要用这种方式写
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
