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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.ui.AppBarManager;
import mika.com.android.ac.ui.AppBarWrapperLayout;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.TransitionUtils;

public class BroadcastListActivityFragment extends Fragment implements AppBarManager {

    private static final String KEY_PREFIX = BroadcastListActivityFragment.class.getName() + '.';

    private static final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    private static final String EXTRA_USER = KEY_PREFIX + "user";
    private static final String EXTRA_TOPIC = KEY_PREFIX + "topic";

    @BindView(R.id.appBarWrapper)
    AppBarWrapperLayout mAppBarWrapperLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private String mUserIdOrUid;
    private User mUser;
    private String mTopic;

    public static BroadcastListActivityFragment newInstance(String userIdOrUid, User user,
                                                            String topic) {
        //noinspection deprecation
        BroadcastListActivityFragment fragment = new BroadcastListActivityFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
        arguments.putParcelable(EXTRA_USER, user);
        arguments.putString(EXTRA_TOPIC, topic);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(String, User, String)} instead.
     */
    public BroadcastListActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mUser = arguments.getParcelable(EXTRA_USER);
        if (mUser != null) {
            mUserIdOrUid = mUser.getIdOrUid();
        } else {
            mUserIdOrUid = arguments.getString(EXTRA_USER_ID_OR_UID);
        }
        mTopic = arguments.getString(EXTRA_TOPIC);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.broadcast_list_activity_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(getTitle());
        activity.setSupportActionBar(mToolbar);

        TransitionUtils.setupTransitionOnActivityCreated(this);

        if (savedInstanceState == null) {
            FragmentUtils.add(BroadcastListFragment.newInstance(mUserIdOrUid, mTopic), this,
                    R.id.broadcast_list_fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getTitle() {
        // TODO: Load user.
        if (mUser != null) {
            return getString(R.string.broadcast_list_title_user_format, mUser.name);
        } else if (!TextUtils.isEmpty(mTopic)) {
            return getString(R.string.broadcast_list_title_topic_format, mTopic);
        } else {
            return getString(R.string.broadcast_list_title_default);
        }
    }

    @Override
    public void showAppBar() {
        mAppBarWrapperLayout.show();
    }

    @Override
    public void hideAppBar() {
        mAppBarWrapperLayout.hide();
    }
}
