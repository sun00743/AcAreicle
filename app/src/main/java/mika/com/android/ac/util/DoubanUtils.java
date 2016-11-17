/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import mika.com.android.ac.network.api.info.apiv2.User;

public class DoubanUtils {

    private DoubanUtils() {}

    public static String getAtUserString(String userIdOrUid) {
        return '@' + userIdOrUid + ' ';
    }

    public static String getAtUserString(User user) {
        //noinspection deprecation
        return getAtUserString(user.uid);
    }
}
