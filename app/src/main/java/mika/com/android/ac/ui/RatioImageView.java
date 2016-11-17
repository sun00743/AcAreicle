/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import mika.com.android.ac.util.MathUtils;

/**
 * An ImageView that measures with a ratio. Also sets scaleType to centerCrop.
 */
public class RatioImageView extends ImageView {

    private float mRatio;

    public RatioImageView(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setScaleType(ScaleType.CENTER_CROP);
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        if (mRatio != ratio) {
            mRatio = ratio;
            requestLayout();
            invalidate();
        }
    }

    public void setRatio(float width, float height) {
        setRatio(width / height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mRatio > 0) {
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                int height = MeasureSpec.getSize(heightMeasureSpec);
                int width = Math.round(mRatio * height);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    width = MathUtils.clamp(width, getMinimumWidth(), getMaxWidth());
                    if (getAdjustViewBounds()) {
                        width = Math.min(width, getMeasuredWidth());
                    }
                }
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            } else {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = Math.round(width / mRatio);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    height = MathUtils.clamp(height, getMinimumHeight(), getMaxHeight());
                    if (getAdjustViewBounds()) {
                        height = Math.min(height, getMeasuredHeight());
                    }
                }
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
