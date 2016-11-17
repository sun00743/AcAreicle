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
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewUtils {

    private RecyclerViewUtils() {}

    public static Context getContext(RecyclerView.ViewHolder holder) {
        return holder.itemView.getContext();
    }

    /**
     * recyclerView的第一个child是否到达recyclerView的父view的顶部
     * @param recyclerView
     * @return
     */
    public static boolean hasFirstChildReachedTop(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View firstChild = layoutManager.findViewByPosition(0);
        if (firstChild != null) {
            return firstChild.getTop() <= 0;
        } else {
            return layoutManager.getChildCount() > 0;
        }
    }
}
