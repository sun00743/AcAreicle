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
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class AnimateCompoundDrawableButton extends AppCompatButton {

    public AnimateCompoundDrawableButton(Context context) {
        super(context);
    }

    public AnimateCompoundDrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimateCompoundDrawableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);

        startIfAnimatable(left);
        startIfAnimatable(top);
        startIfAnimatable(right);
        startIfAnimatable(bottom);
    }

    @Override
    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);

        startIfAnimatable(start);
        startIfAnimatable(top);
        startIfAnimatable(end);
        startIfAnimatable(bottom);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Makes a copy of the array, however we cannot do this otherwise.
        for (Drawable drawable : getCompoundDrawables()) {
            startIfAnimatable(drawable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Makes a copy of the array, however we cannot do this otherwise.
        for (Drawable drawable : getCompoundDrawables()) {
            stopIfAnimatable(drawable);
        }
    }

    private void startIfAnimatable(Drawable drawable) {
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void stopIfAnimatable(Drawable drawable) {
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        }
    }
}
