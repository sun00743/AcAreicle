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
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

public class MaxDimensionHelper {

    private static final int[] STYLEABLE = new int[] {
            android.R.attr.maxWidth,
            android.R.attr.maxHeight
    };
    private static final int STYLEABLE_ANDROID_MAX_WIDTH = 0;
    private static final int STYLEABLE_ANDROID_MAX_HEIGHT = 1;

    private Delegate mDelegate;

    private int mMaxWidth;
    private int mMaxHeight;

    public MaxDimensionHelper(Delegate delegate) {
        mDelegate = delegate;
    }

    public void onInit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, STYLEABLE,
                defStyleAttr, defStyleRes);
        mMaxWidth = a.getDimensionPixelSize(STYLEABLE_ANDROID_MAX_WIDTH, -1);
        mMaxHeight = a.getDimensionPixelSize(STYLEABLE_ANDROID_MAX_HEIGHT, -1);
        a.recycle();
    }

    public void onMeasure(int widthSpec, int heightSpec) {

        if (mMaxWidth >= 0) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(
                    Math.min(View.MeasureSpec.getSize(widthSpec), mMaxWidth),
                    View.MeasureSpec.getMode(widthSpec));
        }

        if (mMaxHeight >= 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                    Math.min(View.MeasureSpec.getSize(heightSpec), mMaxHeight),
                    View.MeasureSpec.getMode(heightSpec));
        }

        mDelegate.superOnMeasure(widthSpec, heightSpec);
    }

    public interface Delegate {
        void superOnMeasure(int widthSpec, int heightSpec);
    }
}
