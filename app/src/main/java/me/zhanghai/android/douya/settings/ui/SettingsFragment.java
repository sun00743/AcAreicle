package me.zhanghai.android.douya.settings.ui;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;

import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.db.AcerDB;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String LOGOUT = "key_logout";
    private static final String LOGOUT_LOGOUT = "key_logout_logout";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        if(!DouyaApplication.LOGIN){
            PreferenceCategory logout = (PreferenceCategory) findPreference(LOGOUT);
            getPreferenceScreen().removePreference(logout);
        }else{
            Preference logout = findPreference(LOGOUT_LOGOUT);
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AcerDB(getActivity().getApplicationContext()).logout();
                    DouyaApplication.LOGIN = false;
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
