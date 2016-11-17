/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.support.v7.widget.RecyclerView;

public abstract class OnHorizontalScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollHorizontally(-1)) {
            onScrolledToLeft();
        } else if (!recyclerView.canScrollHorizontally(1)) {
            onScrolledToRight();
        } else if (dx < 0) {
            onScrolledLeft();
        } else if (dx > 0) {
            onScrolledRight();
        }
    }

    public void onScrolledLeft() {}

    public void onScrolledRight() {}

    public void onScrolledToLeft() {}

    public void onScrolledToRight() {}
}
