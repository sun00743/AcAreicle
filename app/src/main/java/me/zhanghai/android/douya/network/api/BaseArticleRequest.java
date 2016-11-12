package me.zhanghai.android.douya.network.api;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import java.util.Map;

import me.zhanghai.android.douya.util.Connectivity;

/**
 *
 */
public abstract class BaseArticleRequest<T> extends me.zhanghai.android.douya.network.Request<T> {
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
