/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.account.info;

import mika.com.android.ac.BuildConfig;

public class AccountContract {

    public static final String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID;

    public static final String AUTH_TOKEN_TYPE = BuildConfig.APPLICATION_ID;

    public static final String KEY_USER_NAME = BuildConfig.APPLICATION_ID + ".user_name";
    public static final String KEY_USER_ID = BuildConfig.APPLICATION_ID + ".user_id";
    public static final long INVALID_USER_ID = -1;
    public static final String KEY_REFRESH_TOKEN = BuildConfig.APPLICATION_ID
            + ".refresh_token";
    public static final String KEY_USER_INFO = BuildConfig.APPLICATION_ID + ".user_info";

    private static boolean login = false;

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        AccountContract.login = login;
    }

    private AccountContract() {}
}
