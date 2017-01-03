package mika.com.android.ac.network.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.api.info.acapi.QuoteResult;
import mika.com.android.ac.util.Connectivity;

/**
 * Created by mika on 2016/12/14.
 */

public class QuoteRequest extends Request {
    private static final String QUOTE_URL = "http://mobile.app.acfun.cn/comment/at/list";
    private String pageNO;
    private String pageSize;

    public QuoteRequest(int pageNO, int pageSize) {
        super(Method.GET, QUOTE_URL);
        this.pageNO = String.valueOf(pageNO);
        this.pageSize = String.valueOf(pageSize);
        addParams(new HashMap());
        addHeaders(new HashMap());
    }

    @Override
    protected Response<QuoteResult> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, "utf-8");
            QuoteResult result = new Gson().fromJson(data, QuoteResult.class);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Request addParams(Map params) {
        params.put(Connectivity.PAGE_NO, pageNO);
        params.put(Connectivity.PAGE_SIZE, pageSize);
        params.put(Connectivity.USER_ID, String.valueOf(AcWenApplication.getInstance().getAcer().userId));
        params.put(Connectivity.ACCESS_TOKEN, AcWenApplication.getInstance().getAcer().access_token);
        return super.addParams(params);
    }

    @Override
    public Request addHeaders(Map headers) {
        headers.put(Connectivity.VERSION, "4.3.3");
        return super.addHeaders(headers);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
