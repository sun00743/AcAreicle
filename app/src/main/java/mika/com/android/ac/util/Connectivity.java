/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mika.com.android.ac.AcWenApplication;

public class Connectivity {
    private static final String DEFAULT_CACHE_DIR = "acfunArt";
    public static final String UA = "acfun/1.0 (Linux; U; Android "+ Build.VERSION.RELEASE+"; "+ Build.MODEL+"; "+ Locale.getDefault().getLanguage()+"-"+ Locale.getDefault().getCountry().toLowerCase()+") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";
    public static final Map<String,String> UA_MAP = new HashMap<>();
    private static final String TAG = Connectivity.class.getSimpleName();
    static{
        UA_MAP.put("User-Agent", UA);
        UA_MAP.put("deviceType", "1");
    }
    public static final String PAGE_NO = "pageNo";
    public static final String PAGE_SIZE = "pageSize";
    public static final String USER_ID = "userId";
    public static final String ACCESS_TOKEN = "access_token";;
    public static final String VERSION = "appVersion";
    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     * 
     * @param stack
     *            An {@link HttpStack} to use for the network, or null for
     *            default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(HttpStack stack) {

        File cacheDir = AcWenApplication.isExternalStorageAvailable() ? AcWenApplication
                .getExternalCacheFiledir(DEFAULT_CACHE_DIR) : new File(AcWenApplication
                .getInstance().getCacheDir(), DEFAULT_CACHE_DIR);

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // FIXME: dead code
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See:
                // http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(UA));
            }
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir),
                network);
        queue.start();

        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     * 
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue() {
        return newRequestQueue(null);
    }
    
//    public static int request(HttpMethodBase httpMethod, String host, int port, String protocal, Cookie[] cookies)
//            throws HttpException, IOException {
//        HttpClient client = new HttpClient();
//        client.getParams().setParameter("http.protocol.single-cookie-header", true);
//        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//        client.getHttpConnectionManager().getParams().setConnectionTimeout(4000);
//        client.getHostConfiguration().setHost(host, port == 0 ? 80 : port, protocal == null ? "http" : protocal);
//        if(cookies != null){
//            HttpState state = new HttpState();
//            state.addCookies(cookies);
//            client.setState(state);
//        }
//        return client.executeMethod(httpMethod);
//    }

    public static String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=utf-8";

//    public static int doPost(PostMethod post, String host, int port, String protocal, Cookie[] cks)
//            throws HttpException, IOException {
//        return request(post, host, port, protocal, cks);
//    }
//
//    public static int doPost(PostMethod post, String host, Cookie[] cks) throws HttpException, IOException {
//        return doPost(post, host, 0, null, cks);
//    }
//
//    public static JSONObject postResultJson(String path, String host, NameValuePair[] nps, Cookie[] cks) {
//        if (TextUtils.isEmpty(path))
//            throw new NullPointerException("path cannot be null!");
//        PostMethod post = new PostMethod(path);
//        if (nps != null) {
//            post.setRequestBody(nps);
//            post.setRequestHeader("Content-Type", CONTENT_TYPE_FORM);
//        }
//        try {
//            int state = Connectivity.doPost(post, host, cks);
//            if (state == 200) {
//                String json = post.getResponseBodyAsString();
//                JSONObject re = JSON.parseObject(json);
//                return re;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "try to post Result Json :"+path ,e);
//        }
//        return null;
//    }

    /*……………………………………………………………………………………………………………………………………………………………………*/
//    public static int doGet(GetMethod get, String host, int port, String protocal, Cookie[] cookies)
//            throws HttpException, IOException {
//        return request(get, host, port == 0 ? 80 : port, protocal == null ? "http" : protocal, cookies);
//    }
//
//    public static int doGet(GetMethod get, Cookie[] cookies) throws HttpException, IOException {
//        return doGet(get, ArticleApi.getDomainRoot(null), 0, null, cookies);
//    }
//
//    public static String doGet(String url, String queryString, Cookie[] cookies) {
//        if (TextUtils.isEmpty(url))
//            throw new NullPointerException("url cannot be null!");
//        GetMethod get = new GetMethod(url);
//        get.setRequestHeader("User-Agent", UA);
//        if(queryString != null)
//            get.setQueryString(queryString);
//        try {
//            int state = doGet(get, cookies);
//            if (state == 200) {
//                return readData(get.getResponseBodyAsStream(),"utf-8");
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "try to get :"+url ,e);
//        }
//        return null;
//    }
    private static final int BUFF_SIZE = 1 << 13;
    private static String readData(InputStream in, String encoding) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer= new byte[BUFF_SIZE];
        int len = -1;
        while((len = in.read(buffer))!=-1){
            baos.write(buffer, 0, len);
        }
        in.close();
        return new String(baos.toByteArray(),encoding);
    }

//    public static JSONObject getResultJson(String url, String queryString, Cookie[] cookies) {
//        String result = doGet(url, queryString, cookies);
//        try {
//            return TextUtils.isEmpty(result) ? null : JSON.parseObject(result);
//        } catch (JSONException e) {
//            Log.e(TAG, "try to get Result Json :"+url ,e);
//            return null;
//        }
//    }
    
    public static boolean isWifiConnected(Context context){
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }
    
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return (info != null) && (info.isConnected());
    }
    
    public static Cache.Entry newCache(NetworkResponse response, long maxAge){
        long now = System.currentTimeMillis();
        if(maxAge == 0) maxAge = 60;
        Map<String, String> headers = response.headers;

        long serverDate = 0;
        long softExpire = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        softExpire = now + maxAge * 1000;
        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = entry.softTtl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;
        return entry;
    }
    
    public static HttpURLConnection openDefaultConnection(URL url, int connectTimeOut, int readTimeOut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(connectTimeOut);
        connection.setReadTimeout(readTimeOut);
        connection.setUseCaches(false);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
        return connection;
    }
}
