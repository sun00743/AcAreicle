/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FlexibleSpaceContentRecyclerView extends RecyclerView
        implements FlexibleSpaceContentView {

    private int mScroll;

    public FlexibleSpaceContentRecyclerView(Context context) {
        super(context);

        init();
    }

    public FlexibleSpaceContentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FlexibleSpaceContentRecyclerView(Context context, @Nullable AttributeSet attrs,
                                            int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        // Do not save the current scroll position. Always store scrollY as 0 and delegate
        // responsibility of saving state to FlexibleSpaceLayout.
        setSaveEnabled(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        mScroll += dy;
    }

    @Override
    public void scrollTo(int scroll) {
        scrollBy(0, scroll - mScroll);
    }

    public int getScroll() {
        return mScroll;
    }
}
