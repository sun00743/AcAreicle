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
import android.support.annotation.IntDef;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mika.com.android.ac.util.ViewUtils;

public class ContentStateLayout extends FrameLayout {

    @IntDef({STATE_LOADING, STATE_CONTENT, STATE_EMPTY, STATE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public static final int STATE_LOADING = 0;
    public static final int STATE_CONTENT = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;

    private int mLoadingViewId;
    private int mContentViewId;
    private int mEmptyViewId;
    private int mErrorViewId;

    private View mLoadingView;
    private View mContentView;
    private View mEmptyView;
    private View mErrorView;

    private boolean mAnimationEnabled;

    public ContentStateLayout(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public ContentStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public ContentStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContentStateLayout(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                mika.com.android.ac.R.styleable.ContentStateLayout, defStyleAttr, defStyleRes);
        mLoadingViewId = a.getResourceId(mika.com.android.ac.R.styleable.ContentStateLayout_loadingView, mika.com.android.ac.R.id.loading);
        mContentViewId = a.getResourceId(mika.com.android.ac.R.styleable.ContentStateLayout_contentView, mika.com.android.ac.R.id.content);
        mEmptyViewId = a.getResourceId(mika.com.android.ac.R.styleable.ContentStateLayout_emptyView, mika.com.android.ac.R.id.empty);
        mErrorViewId = a.getResourceId(mika.com.android.ac.R.styleable.ContentStateLayout_errorView, mika.com.android.ac.R.id.error);
        mAnimationEnabled = a.getBoolean(mika.com.android.ac.R.styleable.ContentStateLayout_animationEnabled, true);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mLoadingView = findChildById(mLoadingViewId);
        mContentView = findChildById(mContentViewId);
        mEmptyView = findChildById(mEmptyViewId);
        mErrorView = findChildById(mErrorViewId);

        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(getContext())
                    .inflate(mika.com.android.ac.R.layout.content_layout_default_loading_view, this, false);
            addView(mLoadingView);
        }

        setViewVisible(mLoadingView, false, false);
        setViewVisible(mContentView, false, false);
        setViewVisible(mEmptyView, false, false);
        setViewVisible(mErrorView, false, false);
    }

    private View findChildById(int id) {
        for (int i = 0, count = getChildCount(); i < count; ++i) {
            View child = getChildAt(i);
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public boolean isAnimationEnabled() {
        return mAnimationEnabled;
    }

    public void setAnimationEnabled(boolean enabled) {
        mAnimationEnabled = enabled;
    }

    public void setState(@State int state) {
        setViewVisible(mLoadingView, state == STATE_LOADING);
        setViewVisible(mContentView, state == STATE_CONTENT);
        setViewVisible(mEmptyView, state == STATE_EMPTY);
        setViewVisible(mErrorView, state == STATE_ERROR);
    }

    public void setLoading() {
        setState(STATE_LOADING);
    }

    public void setLoaded(boolean hasContent) {
        setState(hasContent ? STATE_CONTENT : STATE_EMPTY);
    }

    public void setError() {
        setState(STATE_ERROR);
    }

    private void setViewVisible(View view, boolean visible, boolean animate) {

        if (view == null) {
            if (visible) {
                // TODO: Be more detailed.
                throw new IllegalStateException("Missing view when setting to visible");
            } else {
                return;
            }
        }

        if (animate) {
            ViewUtils.fadeToVisibility(view, visible);
        } else {
            ViewUtils.setVisibleOrGone(view, visible);
        }
    }

    private void setViewVisible(View view, boolean visible) {
        setViewVisible(view, visible, mAnimationEnabled);
    }
}
