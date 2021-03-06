/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.settings.info;

import mika.com.android.ac.R;

public class Settings {

    public static final SettingsEntries.StringSettingsEntry ID = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_login_id, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry PWD = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_login_pwd, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry API_KEY = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_api_key, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry API_SECRET = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_api_secret, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry ACTIVE_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_active_account_name, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry RECENT_ONE_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_recent_one_account_name, R.string.pref_default_value_empty_string);

    public static final SettingsEntries.StringSettingsEntry RECENT_TWO_ACCOUNT_NAME = new SettingsEntries.StringSettingsEntry(
            R.string.pref_key_recent_two_account_name, R.string.pref_default_value_empty_string);
    /**
     * auto refresh
     */
    public static final SettingsEntries.BooleanSettingsEntry AUTO_REFRESH_HOME = new SettingsEntries.BooleanSettingsEntry(
            R.string.pref_key_auto_refresh_home, R.bool.pref_default_value_auto_refresh_home);
    /**
     * no picture
     */
    public static final SettingsEntries.BooleanSettingsEntry NO_PICTURE = new SettingsEntries.BooleanSettingsEntry(
            R.string.pref_key_no_picture, R.bool.pref_default_value_no_picture);

    public static final SettingsEntries.BooleanSettingsEntry SHOW_TITLE_FOR_LINK_ENTITY = new SettingsEntries.BooleanSettingsEntry(
            R.string.pref_key_show_title_for_link_entity,
            R.bool.pref_default_value_show_title_for_link_entity);

    public enum OpenUrlWithMethod {
        WEBVIEW,
        INTENT,
        CUSTOM_TABS
    }

    /**
     * open url
     */
    public static final SettingsEntries.EnumSettingsEntry<OpenUrlWithMethod> OPEN_URL_WITH_METHOD =
            new SettingsEntries.EnumSettingsEntry<>(R.string.pref_key_open_url_with,
                    R.string.pref_default_value_open_url_with, OpenUrlWithMethod.class);

    public static final SettingsEntries.BooleanSettingsEntry ALWAYS_COPY_TO_CLIPBOARD_AS_TEXT =
            new SettingsEntries.BooleanSettingsEntry(R.string.pref_key_always_copy_to_clipboard_as_text,
                    R.bool.pref_default_value_always_copy_to_clipboard_as_text);
}
