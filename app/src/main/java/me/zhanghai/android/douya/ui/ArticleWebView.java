package me.zhanghai.android.douya.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/9/14.
 */

public class ArticleWebView extends WebView {
    public ArticleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArticleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleWebView(Context context) {
        super(context);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }
}
