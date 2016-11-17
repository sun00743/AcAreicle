/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import java.util.Locale;

public class StringUtils {

    private StringUtils() {}

    public static boolean equalsIgnoreCase(String s1, String s2) {
        //noinspection StringEquality
        if (s1 == s2) {
            return true;
        } else if (s1 != null) {
            return s1.equalsIgnoreCase(s2);
        } else {
            return false;
        }
    }

    public static String formatUs(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }
}
