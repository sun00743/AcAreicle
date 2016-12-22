package mika.com.android.ac.util;

/**
 * Created by mika on 2016/12/21.
 */

public class DateUtils {

    public static String formatAgoTimes(long agoTime) {
        long days = agoTime / (1000 * 60 * 60 * 24);
        long hours = agoTime / (1000 * 60 * 60);
        long min = agoTime / (1000 * 60);

        if (days < 1) {
            if (hours < 1) {
                if (min < 1) {
                    return String.valueOf(agoTime + "秒前");
                } else {
                    return String.valueOf(min + "分钟前");
                }
            } else {
                return String.valueOf(hours + "小时前");
            }
        } else {
            return String.valueOf(days + "天前");
        }
    }
}
