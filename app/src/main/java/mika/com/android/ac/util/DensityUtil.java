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

import mika.com.android.ac.AcWenApplication;

public class DensityUtil {
    public static int dip2px(Context context, float dipValue) {
        if(AcWenApplication.density == 1f){
            AcWenApplication.density  = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dipValue * AcWenApplication.density + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        if(AcWenApplication.density == 1f){
            AcWenApplication.density  = context.getResources().getDisplayMetrics().density;
        }
        return (int) (pxValue / AcWenApplication.density + 0.5f);
    }

}
