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
import android.util.AttributeSet;
import android.view.View;

public class FlexibleSpaceContentLayout extends ContentStateLayout
        implements FlexibleSpaceContentView {

    public FlexibleSpaceContentLayout(Context context) {
        super(context);
    }

    public FlexibleSpaceContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexibleSpaceContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlexibleSpaceContentLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getScroll() {
        View contentView = getContentView();
        if (contentView instanceof FlexibleSpaceContentView) {
            return ((FlexibleSpaceContentView) contentView).getScroll();
        } else {
            return contentView.getScrollY();
        }
    }

    @Override
    public void scrollTo(int scroll) {
        View contentView = getContentView();
        if (contentView instanceof FlexibleSpaceContentView) {
            ((FlexibleSpaceContentView) contentView).scrollTo(scroll);
        } else {
            contentView.scrollTo(0, scroll);
        }
    }
}
