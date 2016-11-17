/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import java.util.Map;

import mika.com.android.ac.util.Connectivity;
import mika.com.android.ac.network.Request;

/**
 *
 */
public abstract class BaseArticleRequest<T> extends Request<T> {
    protected final Class<T> mClazz;
    private final Listener<T> mListener;
    protected final Map<String, String> mPostBody;

    /**
     * @param method
     * @param url
     * @param requestBody the form data to post,
     * @param clazz
     * @param listener
     * @param errorListner
     */
    public BaseArticleRequest(int method, String url, Map<String, String> requestBody, Class<T> clazz, Listener<T> listener, ErrorListener errorListner) {
        super(method, url);
        this.mClazz = clazz;
        this.mListener = listener;
        mPostBody = requestBody;
    }

    public BaseArticleRequest(String url, Map<String, String> requestBody, Class<T> clazz, Listener<T> listener, ErrorListener errorListner) {
        this(requestBody == null? Method.GET: Method.POST, url, requestBody, clazz, listener, errorListner);
    }

    public BaseArticleRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListner){
        this(url,null,clazz,listener,errorListner);
    }
    
    @Override
    protected final void deliverResponse(T response) {
        if(response == null) 
            deliverError(new VolleyError("error response !"));
        else if(mListener != null)
            this.mListener.onResponse(response);
    }
    
    @Override
    public Map<String, String> getHeaders() {
        return Connectivity.UA_MAP;
    }
    @Override
    public Map<String, String> getParams() {
        return mPostBody;
    }
}
