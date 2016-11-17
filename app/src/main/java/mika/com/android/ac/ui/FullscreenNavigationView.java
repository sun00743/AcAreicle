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
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;

/**
 * A {@link NavigationView} that draws no scrim and dispatches window insets correctly.
 * <p>
 * {@code android:fitsSystemWindows="true"} will be ignored.
 * </p>
 */
public class FullscreenNavigationView extends NavigationView {

    public FullscreenNavigationView(Context context) {
        super(context);

        init();
    }

    public FullscreenNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FullscreenNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void init() {

        // Required to revert the value of fitsSystemWindows set in super constructor.
        setFitsSystemWindows(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
                public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                    if (getHeaderCount() == 0) {
                        return FullscreenNavigationView.this.onApplyWindowInsets(insets);
                    }
                    return getHeaderView(0).onApplyWindowInsets(insets);
                }
            });
        }
    }
}
