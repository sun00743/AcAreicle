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
import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

/**
 * Use app:tint instead of android:tint for a ColorStateList.
 */
public class TintStateListImageView extends AppCompatImageView {

    private ColorStateList mTintList;

    public TintStateListImageView(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public TintStateListImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public TintStateListImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                mika.com.android.ac.R.styleable.TintStateListImageView, defStyleAttr, defStyleRes);
        mTintList = a.getColorStateList(mika.com.android.ac.R.styleable.TintStateListImageView_tint);
        updateTintColorFilter();
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        updateTintColorFilter();
    }

    public void setColorFilter(ColorStateList tint) {
        mTintList = tint;
        updateTintColorFilter();
    }

    private void updateTintColorFilter() {
        if (mTintList != null) {
            int color = mTintList.getColorForState(getDrawableState(), mTintList.getDefaultColor());
            setColorFilter(color);
        }
    }
}
