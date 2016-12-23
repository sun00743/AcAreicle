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

import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.info.acapi.AcerInfoResult2;
import mika.com.android.ac.util.GsonHelper;

/**
 * Created by mika on 2016/9/26
 *
 */

public class AcerInfoRequest extends Request {

    private static final String ACER_INFO_URL2 = "http://www.acfun.tv/usercard.aspx?uid=";

    public AcerInfoRequest(int userId) {
        super(Method.GET, ACER_INFO_URL2 + userId);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, "utf-8");
            AcerInfoResult2 result = GsonHelper.get().fromJson(data, AcerInfoResult2.class);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
