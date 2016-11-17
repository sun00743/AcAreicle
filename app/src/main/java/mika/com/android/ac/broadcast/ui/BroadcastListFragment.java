/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import mika.com.android.ac.broadcast.content.BroadcastListResource;
import mika.com.android.ac.link.NotImplementedManager;
import mika.com.android.ac.util.FragmentUtils;

public class BroadcastListFragment extends BaseBroadcastListFragment {

    private static final String KEY_PREFIX = BroadcastListFragment.class.getName() + '.';

    private static final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    private static final String EXTRA_TOPIC = KEY_PREFIX + "topic";

    @BindDimen(mika.com.android.ac.R.dimen.toolbar_height)
    int mToolbarHeight;

    private String mUserIdOrUid;
    private String mTopic;

    public static BroadcastListFragment newInstance(String userIdOrUid, String topic) {
        //noinspection deprecation
        BroadcastListFragment fragment = new BroadcastListFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
        arguments.putString(EXTRA_TOPIC, topic);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(String, String)} instead.
     */
    public BroadcastListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mUserIdOrUid = arguments.getString(EXTRA_USER_ID_OR_UID);
        mTopic = arguments.getString(EXTRA_TOPIC);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setPaddingTop(mToolbarHeight);
    }

    @Override
    protected BroadcastListResource onAttachBroadcastListResource() {
        return BroadcastListResource.attachTo(mUserIdOrUid, mTopic, this);
    }

    @Override
    protected void onSendBroadcast() {
        NotImplementedManager.sendBroadcast(mTopic, getActivity());
    }
}
