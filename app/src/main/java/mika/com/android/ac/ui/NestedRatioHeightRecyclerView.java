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

@SuppressWarnings("deprecation")
public class NestedRatioHeightRecyclerView extends RatioHeightRecyclerView {

    public NestedRatioHeightRecyclerView(Context context) {
        super(context);

        init();
    }

    public NestedRatioHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NestedRatioHeightRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setFocusableInTouchMode(false);
    }
}
