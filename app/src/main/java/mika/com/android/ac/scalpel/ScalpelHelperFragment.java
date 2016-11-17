/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.scalpel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import mika.com.android.ac.app.RetainedFragment;
import mika.com.android.ac.eventbus.EventBusUtils;

public class ScalpelHelperFragment extends RetainedFragment {

    private static final String FRAGMENT_TAG = ScalpelHelperFragment.class.getName();

    private boolean mEnabled;

    private boolean mActivityCreated;
    private boolean mInjected;

    /**
     * @deprecated Use {@link #attachTo(Fragment)} instead.
     */
    public static ScalpelHelperFragment attachTo(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        ScalpelHelperFragment fragment = (ScalpelHelperFragment) fragmentManager
                .findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new ScalpelHelperFragment();
            fragmentManager.beginTransaction()
                    .add(fragment, FRAGMENT_TAG)
                    .commit();
        }
        return fragment;
    }

    public static ScalpelHelperFragment attachTo(Fragment fragment) {
        //noinspection deprecation
        return attachTo(fragment.getActivity());
    }

    public static void setEnabled(boolean enabled) {
        EventBusUtils.postAsync(new SetEnabledEvent(enabled));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBusUtils.register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivityCreated = true;
        if (mEnabled) {
            enable();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        EventBusUtils.unregister(this);

        mActivityCreated = false;
        mInjected = false;
    }

    private void setEnabledForActivity(boolean enabled) {
        if (mActivityCreated) {
            if (enabled) {
                enable();
            } else if (mInjected) {
                ScalpelUtils.setEnabled(getActivity(), false);
            }
        }
        mEnabled = enabled;
    }

    private void enable() {
        if (!mInjected) {
            ScalpelUtils.inject(getActivity());
            mInjected = true;
        }
        ScalpelUtils.setEnabled(getActivity(), true);
    }

    @Keep
    public void onEventMainThread(SetEnabledEvent event) {
        setEnabledForActivity(event.enabled);
    }

    private static class SetEnabledEvent {

        public boolean enabled;

        public SetEnabledEvent(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
