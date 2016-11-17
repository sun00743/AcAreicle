/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network;

import android.accounts.Account;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.AndroidAuthenticator;

/**
 * An {@link AndroidAuthenticator} with {@link #getAuthToken()} synchronized.
 */
public class SynchronizedAndroidAuthenticator extends AndroidAuthenticator {

    public SynchronizedAndroidAuthenticator(Context context, Account account,
                                            String authTokenType) {
        super(context, account, authTokenType);
    }

    public SynchronizedAndroidAuthenticator(Context context, Account account, String authTokenType,
                                            boolean notifyAuthFailure) {
        super(context, account, authTokenType, notifyAuthFailure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getAuthToken() throws AuthFailureError {
        return super.getAuthToken();
    }
}
