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
import mika.com.android.ac.network.api.info.acapi.PostCommentResult;
import mika.com.android.ac.util.Connectivity;
import mika.com.android.ac.util.GsonHelper;

public class PostCommentRequest extends Request<PostCommentResult> {
    private static final String CONTENT_TYPE = "Content-Type";

    public PostCommentRequest() {
        super(Method.POST, ApiContract.Request.AcApi.POST_COMMENT);
    }

    public PostCommentRequest(String text, int quotedId, String token, int userId, int contentId) {
        this();
        addParam("text", text);
        addParam("quoteId", Integer.toString(quotedId));
        addParam("contentId", Integer.toString(contentId));
        addParam("source", "mobile");
        addParam("access_token", token);
        addParam("userId", Integer.toString(userId));
        addParam("captcha", "");

        addHeaders(Connectivity.UA_MAP);
        addHeader("Cookie", "JSESSIONID=" + token);
        addHeader("token", token);
        addHeader(Connectivity.UID, Integer.toString(userId));
        addHeader(CONTENT_TYPE, Connectivity.CONTENT_TYPE_FORM);
    }

    @Override
    protected Response<PostCommentResult> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> headers = response.headers;
            String cookies = headers.get("Set-Cookie");
            // 解析服务器返回的cookie值
//            String cookie = parseVolleyCookie(cookies);
            // 存储cookie
//            SharedPreferenceUtil.putString(LoginActivity.this,"cookie", cookie);

            String data = new String(response.data, "UTF-8");
            return Response.success(GsonHelper.get().fromJson(data, PostCommentResult.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
