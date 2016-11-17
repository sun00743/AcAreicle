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
import android.text.TextUtils;
import android.util.AttributeSet;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;

import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.TimeUtils;
import mika.com.android.ac.util.ViewUtils;

public class JoinedAtLocationAutoGoneTextView extends TimeTextView {

    private String mLocation;

    public JoinedAtLocationAutoGoneTextView(Context context) {
        super(context);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr,
                                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setDoubanTime(String doubanTime) {
        throw new UnsupportedOperationException("Use setJoinedAtAndLocation() instead.");
    }

    public void setJoinedAtAndLocation(String doubanTime, String location) {
        mLocation = location;
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setTimeText(doubanTime);
        }
    }

    @Override
    protected String formatTime(ZonedDateTime time) {
        return TimeUtils.formatDate(time, getContext());
    }

    @Override
    protected void setTimeText(String timeText) {
        String text;
        if (!TextUtils.isEmpty(mLocation)) {
            text = getContext().getString(mika.com.android.ac.R.string.profile_joined_at_and_location_format, timeText,
                    mLocation);
        } else {
            text = getContext().getString(mika.com.android.ac.R.string.profile_joined_at_format, timeText);
        }
        setText(text);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        ViewUtils.setVisibleOrGone(this, !TextUtils.isEmpty(text));
    }
}
