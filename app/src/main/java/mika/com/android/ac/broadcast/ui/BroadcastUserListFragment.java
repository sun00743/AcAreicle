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
import android.support.annotation.Keep;

import java.util.List;

import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.user.ui.BaseUserAdapter;
import mika.com.android.ac.user.ui.DialogUserAdapter;
import mika.com.android.ac.user.ui.UserListFragment;
import mika.com.android.ac.util.FragmentUtils;

public abstract class BroadcastUserListFragment extends UserListFragment {

    // Not static because we are to be subclassed.
    private final String KEY_PREFIX = getClass().getName() + '.';

    public final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";

    private Broadcast mBroadcast;

    protected void setArguments(Broadcast broadcast) {
        FragmentUtils.ensureArguments(this)
                .putParcelable(EXTRA_BROADCAST, broadcast);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcast = getArguments().getParcelable(EXTRA_BROADCAST);
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

    @Override
    protected void onUserListUpdated(List<User> userList) {
        if (onUpdateBroadcast(mBroadcast, userList)) {
            EventBusUtils.postAsync(new BroadcastUpdatedEvent(mBroadcast, this));
        }
    }

    @Override
    protected BaseUserAdapter onCreateAdapter() {
        return new DialogUserAdapter();
    }

    protected abstract boolean onUpdateBroadcast(Broadcast broadcast, List<User> userList);

    @Keep
    public void onEventMainThread(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcast.id == mBroadcast.id) {
            mBroadcast = event.broadcast;
        }
    }

    protected Broadcast getBroadcast() {
        return mBroadcast;
    }
}
