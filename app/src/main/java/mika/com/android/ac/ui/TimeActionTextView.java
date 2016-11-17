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
import android.util.AttributeSet;

import org.threeten.bp.format.DateTimeParseException;

import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.TimeUtils;

public class TimeActionTextView extends TimeTextView {

    private String mAction;

    public TimeActionTextView(Context context) {
        super(context);
    }

    public TimeActionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setDoubanTime(String doubanTime) {
        throw new UnsupportedOperationException("Use setDoubanTimeAndAction() instead.");
    }

    /**
     * Should behave the same as {@link Broadcast#getActionWithTime(Context)}.
     */
    public void setDoubanTimeAndAction(String doubanTime, String action) {
        mAction = action;
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setTimeText(doubanTime);
        }
    }

    @Override
    protected void setTimeText(String timeText) {
        setText(getContext().getString(mika.com.android.ac.R.string.broadcast_time_action_format, timeText, mAction));
    }
}
