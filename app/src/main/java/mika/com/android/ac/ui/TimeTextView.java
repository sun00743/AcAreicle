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
import android.util.AttributeSet;
import android.widget.TextView;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;

import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.TimeUtils;

public class TimeTextView extends TextView {

    private static final int UPDATE_TIME_TEXT_INTERVAL_MILLI = 30 * 1000;

    private final Runnable mUpdateTimeTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };

    private ZonedDateTime mTime;

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ZonedDateTime getTime() {
        return mTime;
    }

    public void setTime(ZonedDateTime time) {
        mTime = time;
        updateTimeText();
    }

    /**
     * Should behave the same as {@link TimeUtils#formatDoubanDateTime(String, Context)}.
     */
    public void setDoubanTime(String doubanTime) {
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setText(doubanTime);
        }
    }

    private void updateTimeText() {
        removeCallbacks(mUpdateTimeTextRunnable);
        if (mTime != null) {
            setTimeText(formatTime(mTime));
            postDelayed(mUpdateTimeTextRunnable, UPDATE_TIME_TEXT_INTERVAL_MILLI);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mTime != null) {
            updateTimeText();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        removeCallbacks(mUpdateTimeTextRunnable);
    }

    protected String formatTime(ZonedDateTime time) {
        return TimeUtils.formatDateTime(time, getContext());
    }

    protected void setTimeText(String timeText) {
        setText(timeText);
    }
}
