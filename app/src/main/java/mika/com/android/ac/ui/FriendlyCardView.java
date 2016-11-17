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
import android.support.v7.widget.CardView;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

/**
 * A friendly card view that has consistent padding across API levels.
 */
public class FriendlyCardView extends CardView {

    public FriendlyCardView(Context context) {
        super(context);

        init(null, 0);
    }

    public FriendlyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0);
    }

    public FriendlyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                mika.com.android.ac.R.styleable.CardView, defStyleAttr, mika.com.android.ac.R.style.CardView_Light);
        setMaxCardElevation(a.getDimension(mika.com.android.ac.R.styleable.CardView_cardMaxElevation,
                getCardElevation()));
        a.recycle();

        setUseCompatPadding(true);
        setPreventCornerOverlap(false);

        // User should never click through a card.
        setClickable(true);
    }
}
