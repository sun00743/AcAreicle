/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.scalpel;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.scalpel.ScalpelFrameLayout;

public class ScalpelUtils {

    private ScalpelUtils() {}

    public static void inject(Activity activity) {
        ViewGroup contentLayout = findContentLayout(activity);
        ScalpelFrameLayout scalpelLayout = new ScalpelFrameLayout(activity);
        while (contentLayout.getChildCount() > 0) {
            View view = contentLayout.getChildAt(0);
            contentLayout.removeViewAt(0);
            scalpelLayout.addView(view);
        }
        contentLayout.addView(scalpelLayout);
    }

    public static void setEnabled(Activity activity, boolean enabled) {
        findScalpelLayout(activity).setLayerInteractionEnabled(enabled);
    }

    private static ViewGroup findContentLayout(Activity activity) {
        return (ViewGroup) activity.findViewById(android.R.id.content);
    }

    private static ScalpelFrameLayout findScalpelLayout(Activity activity) {
        return (ScalpelFrameLayout) findContentLayout(activity).getChildAt(0);
    }
}
