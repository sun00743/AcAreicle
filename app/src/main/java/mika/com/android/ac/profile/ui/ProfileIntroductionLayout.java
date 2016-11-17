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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.ui.FriendlyCardView;
import mika.com.android.ac.util.ViewCompat;
import mika.com.android.ac.util.ViewUtils;

public class ProfileIntroductionLayout extends FriendlyCardView {

    @BindView(mika.com.android.ac.R.id.title)
    TextView mTitleText;
    @BindView(mika.com.android.ac.R.id.content)
    TextView mContentText;

    private Listener mListener;

    public ProfileIntroductionLayout(Context context) {
        super(context);

        init();
    }

    public ProfileIntroductionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProfileIntroductionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        ViewUtils.inflateInto(mika.com.android.ac.R.layout.profile_introduction_layout, this);
        ButterKnife.bind(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void bind(String introduction) {
        introduction = introduction.trim();
        if (!TextUtils.isEmpty(introduction)) {
            final String finalIntroduction = introduction;
            mTitleText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCopyText(finalIntroduction);
                }
            });
            ViewCompat.setBackground(mTitleText, ViewUtils.getDrawableFromAttrRes(
                    mika.com.android.ac.R.attr.selectableItemBackground, getContext()));
            mContentText.setText(introduction);
        } else {
            mTitleText.setOnClickListener(null);
            mTitleText.setClickable(false);
            ViewCompat.setBackground(mTitleText, null);
            mContentText.setText(mika.com.android.ac.R.string.profile_introduction_empty);
        }
    }

    public void bind(UserInfo userInfo) {
        bind(userInfo.introduction);
    }

    private void onCopyText(String text) {
        if (mListener != null) {
            mListener.onCopyText(text);
        }
    }

    public interface Listener {
        void onCopyText(String text);
    }
}
