/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.credential;

import mika.com.android.ac.AcWenApplication;

public final class ApiCredential {

    private ApiCredential() {}

    public static class Frodo {
        public static String KEY = HackyApiCredentialHelper.getApiKey();
        public static String SECRET = HackyApiCredentialHelper.getApiSecret();
    }

    public static class ApiV2 {
        public static String KEY = HackyApiCredentialHelper.getApiKey();
        public static String SECRET = HackyApiCredentialHelper.getApiSecret();
    }
}

class HackyApiCredentialHelper {

    public static String getApiKey() {
        return ApiCredentialManager.getApiKey(AcWenApplication.getInstance());
    }

    public static String getApiSecret() {
        return ApiCredentialManager.getApiSecret(AcWenApplication.getInstance());
    }
}
