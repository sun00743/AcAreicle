/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.content.Context;

public class DensityUtil {

    public static float getDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

}
