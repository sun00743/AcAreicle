/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.content.Context;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.DateTimeParseException;

import mika.com.android.ac.ui.TimeTextView;

public class TimeUtils {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static final DateTimeFormatter DOUBAN_DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .append(DateTimeFormatter.ISO_LOCAL_DATE)
                    .appendLiteral(' ')
                    .append(DateTimeFormatter.ISO_LOCAL_TIME)
                    .toFormatter()
                    .withChronology(IsoChronology.INSTANCE);

    private static final ZoneId DOUBAN_ZONED_ID = ZoneId.of("Asia/Shanghai");

    private static final Duration JUST_NOW_DURATION = Duration.ofMinutes(1);
    private static final Duration MINUTE_PATTERN_DURATION = Duration.ofHours(1);
    private static final Duration HOUR_PATTERN_DURATION = Duration.ofHours(2);

    /**
     * @throws DateTimeParseException
     */
    public static ZonedDateTime parseDoubanDateTime(String doubanDateTime) {
        return ZonedDateTime.of(LocalDateTime.parse(doubanDateTime, DOUBAN_DATE_TIME_FORMATTER),
                DOUBAN_ZONED_ID);
    }

    public static String formatDateTime(ZonedDateTime dateTime, Context context) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(dateTime.getZone());
        LocalDate date = dateTime.toLocalDate();
        LocalDate nowDate = now.toLocalDate();
        if (date.equals(nowDate)) {
            Duration duration = Duration.between(dateTime, now);
            if (duration.compareTo(Duration.ZERO) > 0) {
                if (duration.compareTo(JUST_NOW_DURATION) < 0) {
                    return context.getString(mika.com.android.ac.R.string.just_now);
                } else if (duration.compareTo(MINUTE_PATTERN_DURATION) < 0) {
                    return context.getString(mika.com.android.ac.R.string.minute_format,
                            Math.round((float) duration.getSeconds() / SECONDS_PER_MINUTE));
                } else if (duration.compareTo(HOUR_PATTERN_DURATION) < 0) {
                    return context.getString(mika.com.android.ac.R.string.hour_format,
                            Math.round((float) duration.getSeconds() / SECONDS_PER_HOUR));
                }
            }
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.today_hour_minute_pattern))
                    .format(dateTime);
        }
        if (date.plusDays(1).equals(nowDate)) {
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.yesterday_hour_minute_pattern))
                    .format(dateTime);
        } else if (date.getYear() == nowDate.getYear()) {
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.month_day_hour_minute_pattern))
                    .format(dateTime);
        } else {
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.date_hour_minute_pattern))
                    .format(dateTime);
        }
    }

    /**
     * Use {@link TimeTextView} instead if the text is to be set on a
     * {@code TextView}.
     */
    public static String formatDoubanDateTime(String doubanDateTime, Context context) {
        try {
            return formatDateTime(parseDoubanDateTime(doubanDateTime), context);
        } catch (DateTimeParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanDateTime);
            e.printStackTrace();
            return doubanDateTime;
        }
    }

    public static String formatDate(LocalDate date, ZoneId zone, Context context) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(zone);
        LocalDate nowDate = now.toLocalDate();
        if (date.equals(nowDate)) {
            return context.getString(mika.com.android.ac.R.string.today);
        }
        if (date.plusDays(1).equals(nowDate)) {
            return context.getString(mika.com.android.ac.R.string.yesterday);
        } else if (date.getYear() == nowDate.getYear()) {
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.month_day_pattern))
                    .format(date);
        } else {
            return DateTimeFormatter
                    .ofPattern(context.getString(mika.com.android.ac.R.string.date_pattern))
                    .format(date);
        }
    }

    public static String formatDate(ZonedDateTime dateTime, Context context) {
        return formatDate(dateTime.toLocalDate(), dateTime.getZone(), context);
    }
}
