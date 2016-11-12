package me.zhanghai.android.douya.network.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import me.zhanghai.android.douya.network.Request;
import me.zhanghai.android.douya.network.api.info.acapi.AcerInfoResult2;
import me.zhanghai.android.douya.util.GsonHelper;

/**
 * Created by Administrator on 2016/9/26.
 */

public class AcerInfoRequest extends Request {

    private static final String ACER_INFO_URL = "http://api.acfun.tv/apiserver/profile?userId=";
    private static final String ACER_INFO_URL2 = "http://www.acfun.tv/usercard.aspx?uid=";

    public AcerInfoRequest(int userId){
        this(0,null,userId);
    }

    public AcerInfoRequest(int method, String url, int userId) {
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
