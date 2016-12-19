/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.settings.ui;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;

import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.db.AcerDB;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String LOGOUT = "key_logout";
    private static final String LOGOUT_LOGOUT = "key_logout_logout";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        if(!AcWenApplication.LOGIN){
            PreferenceCategory logout = (PreferenceCategory) findPreference(LOGOUT);
            getPreferenceScreen().removePreference(logout);
        }else{
            Preference logout = findPreference(LOGOUT_LOGOUT);
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AcerDB(getActivity().getApplicationContext()).logout();
                    AcWenApplication.LOGIN = false;
                    getActivity().finish();
                    return false;
                }
            });
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (!LicensesDialogPreference.onDisplayPreferenceDialog(this, preference)) {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
