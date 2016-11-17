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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.util.ViewUtils;

public class CardIconButton extends LinearLayout {

    @BindView(mika.com.android.ac.R.id.cardiconbutton_image)
    ImageView mImage;
    @BindView(mika.com.android.ac.R.id.cardiconbutton_text)
    TextView mText;

    public CardIconButton(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public CardIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public CardIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardIconButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setClickable(true);
        setFocusable(true);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);

        ViewUtils.inflateInto(mika.com.android.ac.R.layout.card_icon_button, this);
        ButterKnife.bind(this);

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                mika.com.android.ac.R.styleable.CardIconButton, defStyleAttr, defStyleRes);
        Drawable src = a.getDrawable(mika.com.android.ac.R.styleable.CardIconButton_android_src);
        if (src != null) {
            mImage.setImageDrawable(src);
        }
        CharSequence text = a.getText(mika.com.android.ac.R.styleable.CardIconButton_android_text);
        setText(text);
        a.recycle();
    }

    public ImageView getImageView() {
        return mImage;
    }

    public TextView getTextView() {
        return mText;
    }

    public void setIcon(Drawable icon) {
        mImage.setImageDrawable(icon);
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }
}
