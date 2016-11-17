/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;

public class ColorUtils {

    private ColorUtils() {}

    @ColorInt
    public static int blendAlphaComponent(@ColorInt int color,
                                          @IntRange(from = 0x0, to = 0xFF) int alpha) {
        return android.support.v4.graphics.ColorUtils.setAlphaComponent(color,
                Color.alpha(color) * alpha / 0xFF);
    }
}
