/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.Window;

public class StatusBarColorUtils {

    private static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();

    public static void animateTo(Window window, int color, int duration) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        ObjectAnimator animator = ObjectAnimator.ofInt(window, "statusBarColor",
                    window.getStatusBarColor(), color);
        animator.setEvaluator(sArgbEvaluator);
        animator.setDuration(duration);
        animator.setAutoCancel(true);
        animator.start();
    }

    public static void animateTo(int color, Activity activity) {
        animateTo(activity.getWindow(), color, ViewUtils.getShortAnimTime(activity));
    }

    public static void set(Window window, int color) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        window.setStatusBarColor(color);
    }

    public static void set(int color, Activity activity) {
        set(activity.getWindow(), color);
    }
}
