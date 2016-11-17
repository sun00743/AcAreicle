/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.settings.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.AttributeSet;

import de.psdev.licensesdialog.LicensesDialogFragment;

public class LicensesDialogPreference extends DialogPreference {

    // As in PreferenceFragmentCompat, because we want to ensure that at most one dialog is showing.
    private static final String DIALOG_FRAGMENT_TAG =
            "android.support.v7.preference.PreferenceFragment.DIALOG";

    /**
     * Users should override {@link PreferenceFragmentCompat#onDisplayPreferenceDialog(Preference)}
     * and check the return value of this method, only call through to super implementation if
     * {@code false} is returned.
     *
     * @param preferenceFragment The preference fragment
     * @param preference The preference, as in
     * {@link PreferenceFragmentCompat#onDisplayPreferenceDialog(Preference)}
     * @return Whether the call has been handled by this method.
     */
    public static boolean onDisplayPreferenceDialog(PreferenceFragmentCompat preferenceFragment,
                                                    Preference preference) {

        if (preference instanceof LicensesDialogPreference) {
            // getChildFragmentManager() will lead to looking for target fragment in the child
            // fragment manager.
            FragmentManager fragmentManager = preferenceFragment.getFragmentManager();
            if(fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
                LicensesDialogFragment dialogFragment =
                        new LicensesDialogFragment.Builder(preferenceFragment.getActivity())
                                .setNotices(mika.com.android.ac.R.raw.licenses)
                                .setUseAppCompat(true)
                                .build();
                dialogFragment.setTargetFragment(preferenceFragment, 0);
                dialogFragment.show(fragmentManager, DIALOG_FRAGMENT_TAG);
            }
            return true;
        }

        return false;
    }

    public LicensesDialogPreference(Context context) {
        super(context);
    }

    public LicensesDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LicensesDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LicensesDialogPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
