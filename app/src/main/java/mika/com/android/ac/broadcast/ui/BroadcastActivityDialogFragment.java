/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.ui.TabFragmentPagerAdapter;
import mika.com.android.ac.util.FragmentUtils;

public class BroadcastActivityDialogFragment extends AppCompatDialogFragment {

    private static final String KEY_PREFIX = BroadcastActivityDialogFragment.class.getName() + '.';

    public static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";

    @BindView(mika.com.android.ac.R.id.tab)
    TabLayout mTabLayout;
    @BindView(mika.com.android.ac.R.id.viewPager)
    ViewPager mViewPager;
    @BindView(android.R.id.button1)
    Button mPositiveButton;
    @BindView(android.R.id.button2)
    Button mNegativeButton;
    @BindView(android.R.id.button3)
    Button mNeutralButton;

    private TabFragmentPagerAdapter mTabAdapter;

    private Broadcast mBroadcast;

    /**
     * @deprecated Use {@link #newInstance(Broadcast)} instead.
     */
    public BroadcastActivityDialogFragment() {}

    public static BroadcastActivityDialogFragment newInstance(Broadcast broadcast) {
        //noinspection deprecation
        BroadcastActivityDialogFragment fragment = new BroadcastActivityDialogFragment();
        FragmentUtils.ensureArguments(fragment)
                .putParcelable(EXTRA_BROADCAST, broadcast);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcast = getArguments().getParcelable(EXTRA_BROADCAST);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog dialog = (AppCompatDialog) super.onCreateDialog(savedInstanceState);
        // We are using a custom title, as in AlertDialog.
        dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mika.com.android.ac.R.layout.broadcast_activity_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabAdapter = new TabFragmentPagerAdapter(this);
//        mTabAdapter.addTab(new TabFragmentPagerAdapter.FragmentCreator() {
//            @Override
//            public Fragment createFragment() {
//                return BroadcastLikerListFragment.newInstance(mBroadcast);
//            }
//        }, null);
//        mTabAdapter.addTab(new TabFragmentPagerAdapter.FragmentCreator() {
//            @Override
//            public Fragment createFragment() {
//                return BroadcastRebroadcasterListFragment.newInstance(mBroadcast);
//            }
//        }, null);
        updateTabTitle();
        mViewPager.setOffscreenPageLimit(mTabAdapter.getCount() - 1);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPositiveButton.setText(mika.com.android.ac.R.string.ok);
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mNegativeButton.setVisibility(View.GONE);
        mNeutralButton.setVisibility(View.GONE);
    }

    @Override
    public void onStart(){
        super.onStart();

        EventBusUtils.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBusUtils.unregister(this);
    }

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcast.id == mBroadcast.id) {
            mBroadcast = event.broadcast;
            updateTabTitle();
        }
    }

    private void updateTabTitle() {
        mTabAdapter.setPageTitle(mTabLayout, 0, getTabTitle(mBroadcast.likeCount,
                mika.com.android.ac.R.string.broadcast_likers_title_format, mika.com.android.ac.R.string.broadcast_likers_title_empty));
        mTabAdapter.setPageTitle(mTabLayout, 1, getTabTitle(mBroadcast.rebroadcastCount,
                mika.com.android.ac.R.string.broadcast_rebroadcasters_title_format,
                mika.com.android.ac.R.string.broadcast_rebroadcasters_title_empty));
    }

    private CharSequence getTabTitle(int count, int formatResId, int emptyResId) {
        return count > 0 ? getString(formatResId, count) : getString(emptyResId);
    }

    public static void show(Broadcast broadcast, Fragment fragment) {
        BroadcastActivityDialogFragment.newInstance(broadcast)
                .show(fragment.getChildFragmentManager(), null);
    }
}
