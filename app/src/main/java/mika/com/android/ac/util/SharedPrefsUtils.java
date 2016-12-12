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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * SharedPreference 工具类
 */
public class SharedPrefsUtils {

    private SharedPrefsUtils() {}

    /**
     * 获取应用默认sp
     * @param context context
     * @return
     */
    public static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getString(Entry<String> entry, Context context) {
        return getSharedPrefs(context).getString(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static Set<String> getStringSet(Entry<Set<String>> entry, Context context) {
        return getSharedPrefs(context).getStringSet(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static int getInt(Entry<Integer> entry, Context context) {
        return getSharedPrefs(context).getInt(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static long getLong(Entry<Long> entry, Context context) {
        return getSharedPrefs(context).getLong(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static float getFloat(Entry<Float> entry, Context context) {
        return getSharedPrefs(context).getFloat(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static boolean getBoolean(Entry<Boolean> entry, Context context) {
        return getSharedPrefs(context).getBoolean(entry.getKey(context),
                entry.getDefaultValue(context));
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPrefs(context).edit();
    }

    public static void putString(Entry<String> entry, String value, Context context) {
        getEditor(context).putString(entry.getKey(context), value).apply();
    }

    public static void putStringSet(Entry<Set<String>> entry, Set<String> value, Context context) {
        getEditor(context).putStringSet(entry.getKey(context), value).apply();
    }

    public static void putInt(Entry<Integer> entry, int value, Context context) {
        getEditor(context).putInt(entry.getKey(context), value).apply();
    }

    public static void putLong(Entry<Long> entry, long value, Context context) {
        getEditor(context).putLong(entry.getKey(context), value).apply();
    }

    public static void putFloat(Entry<Float> entry, float value, Context context) {
        getEditor(context).putFloat(entry.getKey(context), value).apply();
    }

    public static void putBoolean(Entry<Boolean> entry, boolean value, Context context) {
        getEditor(context).putBoolean(entry.getKey(context), value).apply();
    }

    /**
     * 清除保存的指定内容对象
     * @param entry the entry
     * @param context context
     */
    public static void remove(Entry<?> entry, Context context) {
        getEditor(context).remove(entry.getKey(context)).apply();
    }

    /**
     * 清楚应用保存的所有内容
     * @param context context
     */
    public static void clear(Context context) {
        getEditor(context).clear().apply();
    }

    /**
     * 保存的数据对象，在相应的类或内部类中实现这个接口，然后操作数据
     * @param <T> 对象类型
     */
    public interface Entry<T> {
        /**
         * 设置key方法，组装key
         * @param context context
         * @return String-key
         */
        String getKey(Context context);

        /**
         * 设置当前key默认值
         * @param context context
         * @return <T> defaultValue
         */
        T getDefaultValue(Context context);
    }
}
