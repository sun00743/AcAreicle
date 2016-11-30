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
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.info.acapi.GetBananaResult;
import mika.com.android.ac.util.Connectivity;

public class GetBananaRequest extends Request {

    private static final String GET_GETBANANA_URL = "http://mobile.acfun.tv/banana/getBananaCount.aspx?access_token=";
    private static final String APP_VERSION = "4.3.0";
    private static final String PRODUCT_ID = "2000";

    public GetBananaRequest(String token){
        super(Method.GET, GET_GETBANANA_URL + token);

        addHeader("appVersion", APP_VERSION);
        addHeader("User-Agent", Connectivity.UA);
        addHeader("productId", PRODUCT_ID);
        addHeader("deviceType", "1");
        addHeader("uid", "623674");
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> headers = response.headers;
            String data = new String(response.data, "UTF-8");
            GetBananaResult result = new Gson().fromJson(data, GetBananaResult.class);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
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
