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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import mika.com.android.ac.util.SpanUtils;
import mika.com.android.ac.util.ViewUtils;

public class AutoLinkTextView extends AppCompatTextView {

    public AutoLinkTextView(Context context) {
        super(context);

        init();
    }

    public AutoLinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AutoLinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        if (getAutoLinkMask() != 0) {
            throw new IllegalStateException("Don't set android:autoLink");
        }

        ViewUtils.setTextViewLinkClickable(this);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(SpanUtils.addLinks(text), BufferType.SPANNABLE);
    }
}
