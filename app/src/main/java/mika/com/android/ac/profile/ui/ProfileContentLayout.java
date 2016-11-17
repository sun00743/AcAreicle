/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import mika.com.android.ac.profile.util.ProfileUtils;
import mika.com.android.ac.ui.FlexibleSpaceContentLayout;

public class ProfileContentLayout extends FlexibleSpaceContentLayout {

    @BindDimen(mika.com.android.ac.R.dimen.screen_edge_horizontal_margin)
    int mScreenEdgeHorizontalMargin;
    @BindDimen(mika.com.android.ac.R.dimen.single_line_list_item_height)
    int mSingleLineListItemHeight;
    @BindDimen(mika.com.android.ac.R.dimen.card_vertical_margin)
    int mCardVerticalMargin;
    @BindDimen(mika.com.android.ac.R.dimen.card_shadow_vertical_margin)
    int mCardShadowVerticalMargin;
    @BindDimen(mika.com.android.ac.R.dimen.horizontal_divider_height)
    int mHorizontalDividerHeight;

    private boolean mUseWideLayout;

    public ProfileContentLayout(Context context) {
        super(context);
        init();
    }

    public ProfileContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProfileContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public ProfileContentLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        ButterKnife.bind(this);

        mUseWideLayout = ProfileUtils.shouldUseWideLayout(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mUseWideLayout) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int paddingLeft = ProfileUtils.getAppBarWidth(width, getContext())
                    - mScreenEdgeHorizontalMargin;
            setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            int height = MeasureSpec.getSize(heightMeasureSpec);
            View contentView = getContentView();
            int contentPaddingTop = height * 2 / 5 - mSingleLineListItemHeight
                    - (mCardVerticalMargin - mCardShadowVerticalMargin) - mHorizontalDividerHeight;
            contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop,
                    contentView.getPaddingRight(), contentView.getPaddingBottom());
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
