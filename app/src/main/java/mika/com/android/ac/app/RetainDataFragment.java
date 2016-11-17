/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.util.FragmentUtils;

/**
 * A Fragment that can retain data passed in, and remove them once your instance is recreated.
 *
 * @deprecated Use {@link ResourceFragment} for state.
 */
public class RetainDataFragment extends RetainedFragment {

    private static final String FRAGMENT_TAG = RetainDataFragment.class.getName();

    private Map<String, Object> mData = new HashMap<>();

    public static RetainDataFragment attachTo(FragmentActivity activity) {
        RetainDataFragment fragment = FragmentUtils.findByTag(activity, FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new RetainDataFragment();
            FragmentUtils.add(fragment, activity, FRAGMENT_TAG);
        }
        return fragment;
    }

    /**
     * This attaches the RetainDataFragment to the host Activity, so that it can retain instance.
     * For this reason you need to differentiate your key with other Fragment and the host Activity.
     */
    public static RetainDataFragment attachTo(Fragment fragment) {
        return attachTo(fragment.getActivity());
    }

    public boolean containsKey(String key) {
        return mData.containsKey(key);
    }

    public <T> T remove(String key) {
        //noinspection unchecked
        return (T) mData.remove(key);
    }

    public boolean removeBoolean(String key, boolean defaultValue) {
        Boolean b = remove(key);
        return b != null ? b : defaultValue;
    }

    public void put(String key, Object value) {
        mData.put(key, value);
    }

    public void onDestroy() {
        super.onDestroy();

        mData.clear();
    }
}
