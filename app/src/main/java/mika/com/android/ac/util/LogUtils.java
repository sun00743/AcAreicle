/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "douya";

    private LogUtils() {}

    public static void d(String message) {
        Log.d(TAG, buildMessage(message));
    }

    public static void e(String message) {
        Log.e(TAG, buildMessage(message));
    }

    public static void i(String message) {
        Log.i(TAG, buildMessage(message));
    }

    public static void v(String message) {
        Log.v(TAG, buildMessage(message));
    }

    public static void w(String message) {
        Log.w(TAG, buildMessage(message));
    }

    public static void wtf(String message) {
        Log.wtf(TAG, buildMessage(message));
    }

    public static void println(String message) {
        Log.println(Log.INFO, TAG, message);
    }

    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + caller.getMethodName() + "(): " + rawMessage;
    }
}
