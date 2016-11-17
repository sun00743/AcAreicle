/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import java.nio.charset.Charset;

public class StandardCharsetsCompat {

    private StandardCharsetsCompat() {}

    /**
     * The ISO-8859-1 charset.
     */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    /**
     * The US-ASCII charset.
     */
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    /**
     * The UTF-8 charset.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * The UTF-16 charset.
     */
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    /**
     * The UTF-16BE (big-endian) charset.
     */
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

    /**
     * The UTF-16LE (little-endian) charset.
     */
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
}
