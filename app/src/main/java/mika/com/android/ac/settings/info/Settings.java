/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.settings.info;

public class Settings {

    public static final SettingsEntries.StringSettingsEntry API_KEY = new SettingsEntries.StringSettingsEntry(
            mika.com.android.ac.R.string.pref_key_api_key, mika.com.android.ac.R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry API_SECRET = new SettingsEntries.StringSettingsEntry(
            mika.com.android.ac.R.string.pref_key_api_secret, mika.com.android.ac.R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry ACTIVE_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            mika.com.android.ac.R.string.pref_key_active_account_name, mika.com.android.ac.R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry RECENT_ONE_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            mika.com.android.ac.R.string.pref_key_recent_one_account_name, mika.com.android.ac.R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry RECENT_TWO_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            mika.com.android.ac.R.string.pref_key_recent_two_account_name, mika.com.android.ac.R.string.pref_default_value_empty_string);

    public static final SettingsEntries.BooleanSettingsEntry AUTO_REFRESH_HOME = new SettingsEntries.BooleanSettingsEntry(
            mika.com.android.ac.R.string.pref_key_auto_refresh_home, mika.com.android.ac.R.bool.pref_default_value_auto_refresh_home);

    public static final SettingsEntries.BooleanSettingsEntry SHOW_TITLE_FOR_LINK_ENTITY = new SettingsEntries.BooleanSettingsEntry(
            mika.com.android.ac.R.string.pref_key_show_title_for_link_entity,
            mika.com.android.ac.R.bool.pref_default_value_show_title_for_link_entity);

    public enum OpenUrlWithMethod {
        WEBVIEW,
        INTENT,
        CUSTOM_TABS
    }

    public static final SettingsEntries.EnumSettingsEntry<OpenUrlWithMethod> OPEN_URL_WITH_METHOD =
            new SettingsEntries.EnumSettingsEntry<>(mika.com.android.ac.R.string.pref_key_open_url_with,
                    mika.com.android.ac.R.string.pref_default_value_open_url_with, OpenUrlWithMethod.class);

    public static final SettingsEntries.BooleanSettingsEntry ALWAYS_COPY_TO_CLIPBOARD_AS_TEXT =
            new SettingsEntries.BooleanSettingsEntry(mika.com.android.ac.R.string.pref_key_always_copy_to_clipboard_as_text,
                    mika.com.android.ac.R.bool.pref_default_value_always_copy_to_clipboard_as_text);
}
