package me.zhanghai.android.douya.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by mika on 2016/10/21.
 */

public class DetectsSoftKeyBoardFrameLayout extends FrameLayout {
    private SoftKeyBoardListener listener;

    public DetectsSoftKeyBoardFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetectsSoftKeyBoardFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface SoftKeyBoardListener{
        void onSoftKeyBoardDown(boolean isShowing);
    }

    public void setSoftKeyBoardListener(SoftKeyBoardListener listener){
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity)getContext();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
//                getWindowManager().getDefaultDisplay().getHeight();
        int diff = (screenHeight - statusBarHeight) - height;
        if (listener != null) {
            listener.onSoftKeyBoardDown(diff>128); // assume all soft keyboards are at least 128 pixels high
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
