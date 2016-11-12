package me.zhanghai.android.douya.util;

import android.content.Context;

import me.zhanghai.android.douya.DouyaApplication;

public class DensityUtil {
    public static int dip2px(Context context, float dipValue) {
        if(DouyaApplication.density == 1f){
            DouyaApplication.density  = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dipValue * DouyaApplication.density + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        if(DouyaApplication.density == 1f){
            DouyaApplication.density  = context.getResources().getDisplayMetrics().density;
        }
        return (int) (pxValue / DouyaApplication.density + 0.5f);
    }

}
