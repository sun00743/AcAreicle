/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Authenticator;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;

import java.io.File;

import mika.com.android.ac.DouyaApplication;
import mika.com.android.ac.account.info.AccountContract;
import mika.com.android.ac.account.util.AccountUtils;

public class Volley {

    private static final Volley INSTANCE = new Volley();

    private Authenticator mAuthenticator;
    private RequestQueue mRequestQueue;

    public static Volley getInstance() {
        return INSTANCE;
    }

    private Volley() {
        createAuthenticatorForActiveAccount();
        createAndStartRequestQueue();
    }

    private void createAuthenticatorForActiveAccount() {
        Context context = DouyaApplication.getInstance();
        mAuthenticator = new SynchronizedAndroidAuthenticator(context,
                AccountUtils.getActiveAccount(context), AccountContract.AUTH_TOKEN_TYPE, true);
    }

    public void notifyActiveAccountChanged() {
        createAuthenticatorForActiveAccount();
    }

    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }

    /**
     * @see com.android.volley.toolbox.Volley#newRequestQueue(Context, HttpStack, int)
     */
    private void createAndStartRequestQueue() {
        mRequestQueue = new RequestQueue(new DiskBasedCache(new File(
                DouyaApplication.getInstance().getCacheDir(), "volley")), new BasicNetwork(
                new HurlStack()));
        mRequestQueue.start();
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
        return request;
    }

    public void cancelRequests(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
