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
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.apiv2.Image;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewUtils;

public class ImageLayout extends FrameLayout {

    public static final int FILL_ORIENTATION_HORIZONTAL = 0;
    public static final int FILL_ORIENTATION_VERTICAL = 1;

    @BindView(mika.com.android.ac.R.id.imagelayout_image)
    RatioImageView mImageView;
    @BindView(mika.com.android.ac.R.id.imagelayout_gif)
    ImageView mGifImage;

    public ImageLayout(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setClickable(true);
        setFocusable(true);

        ViewUtils.inflateInto(mika.com.android.ac.R.layout.image_layout, this);
        ButterKnife.bind(this);

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                mika.com.android.ac.R.styleable.ImageLayout, defStyleAttr, defStyleRes);
        int fillOrientation = a.getInt(mika.com.android.ac.R.styleable.ImageLayout_fillOrientation,
                FILL_ORIENTATION_HORIZONTAL);
        a.recycle();

        LayoutParams layoutParams = (LayoutParams) mImageView.getLayoutParams();
        layoutParams.width = fillOrientation == FILL_ORIENTATION_HORIZONTAL ?
                LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT;
        layoutParams.height = fillOrientation == FILL_ORIENTATION_HORIZONTAL ?
                LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT;
        mImageView.setLayoutParams(layoutParams);
    }

    public void loadImage(Image image) {
        ImageUtils.loadImage(mImageView, image);
        ViewUtils.setVisibleOrGone(mGifImage, image.animated);
    }

    public void releaseImage() {
        mImageView.setImageDrawable(null);
    }
}
