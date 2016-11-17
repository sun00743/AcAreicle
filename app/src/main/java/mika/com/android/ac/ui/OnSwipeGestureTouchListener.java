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
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public abstract class OnSwipeGestureTouchListener extends OnGestureTouchListener {

    private final int mMinSwipeDelta;
    private final int mMinSwipeVelocity;
    private final int mMaxSwipeVelocity;

    public OnSwipeGestureTouchListener(Context context, boolean isLongPressEnabled) {
        super(context, isLongPressEnabled);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        // We think a swipe flings a full page.
        //mMinSwipeDelta = configuration.getScaledTouchSlop();
        mMinSwipeDelta = configuration.getScaledPagingTouchSlop();
        mMinSwipeVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaxSwipeVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    // NOTE: see http://stackoverflow.com/questions/937313/android-basic-gesture-detection
    @Override
    public final boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
                                 float velocityY) {

        boolean result = false;
        try {
            float deltaX = event2.getX() - event1.getX();
            float deltaY = event2.getY() - event1.getY();
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);
            float absDeltaX = Math.abs(deltaX);
            float absDeltaY = Math.abs(deltaY);
            if (absDeltaX > absDeltaY) {
                if (absDeltaX > mMinSwipeDelta && absVelocityX > mMinSwipeVelocity
                        && absVelocityX < mMaxSwipeVelocity) {
                    if (deltaX < 0) {
                        onSwipeLeft();
                    } else {
                        onSwipeRight();
                    }
                }
                result = true;
            } else if (absDeltaY > mMinSwipeDelta && absVelocityY > mMinSwipeVelocity
                    && absVelocityY < mMaxSwipeVelocity) {
                if (deltaY < 0) {
                    onSwipeUp();
                } else {
                    onSwipeDown();
                }
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void onSwipeLeft() {}

    public void onSwipeRight() {}

    public void onSwipeUp() {}

    public void onSwipeDown() {}
}
