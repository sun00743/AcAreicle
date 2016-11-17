/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.credential;

import android.content.Context;

import mika.com.android.ac.settings.info.Settings;

class ApiCredentialManager {

    private ApiCredentialManager() {}

    public static String getApiKey(Context context) {
        return Settings.API_KEY.getValue(context);
    }

    public static String getApiSecret(Context context) {
        return Settings.API_SECRET.getValue(context);
    }

    public static void setApiCredential(String apiKey, String apiSecret, Context context) {

        Settings.API_KEY.putValue(apiKey, context);
        Settings.API_SECRET.putValue(apiSecret, context);

        ApiCredential.Frodo.KEY = apiKey;
        ApiCredential.Frodo.SECRET = apiSecret;
        ApiCredential.ApiV2.KEY = apiKey;
        ApiCredential.ApiV2.SECRET = apiSecret;
    }
}
