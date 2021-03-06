/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import java.util.List;

import mika.com.android.ac.broadcast.content.BroadcastRebroadcasterListResource;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.user.content.BaseUserListResource;

public class BroadcastRebroadcasterListFragment extends BroadcastUserListFragment {

    /**
     * @deprecated Use {@link #newInstance(Broadcast)} instead.
     */
    public BroadcastRebroadcasterListFragment() {}

    public static BroadcastRebroadcasterListFragment newInstance(Broadcast broadcast) {
        //noinspection deprecation
        BroadcastRebroadcasterListFragment fragment = new BroadcastRebroadcasterListFragment();
        fragment.setArguments(broadcast);
        return fragment;
    }

    @Override
    protected BaseUserListResource onAttachUserListResource() {
        return BroadcastRebroadcasterListResource.attachTo(getBroadcast().id, this);
    }

    @Override
    protected boolean onUpdateBroadcast(Broadcast broadcast, List<User> userList) {
        if (broadcast.rebroadcastCount < userList.size()) {
            broadcast.rebroadcastCount = userList.size();
            return true;
        }
        return false;
    }
}
