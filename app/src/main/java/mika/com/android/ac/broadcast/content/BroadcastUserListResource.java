/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import mika.com.android.ac.user.content.RawUserListResource;
import mika.com.android.ac.util.FragmentUtils;

public abstract class BroadcastUserListResource extends RawUserListResource {

    // Not static because we are to be subclassed.
    private final String KEY_PREFIX = getClass().getName() + '.';

    public final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";

    protected void setArguments(long broadcastId) {
        FragmentUtils.ensureArguments(this)
                .putLong(EXTRA_BROADCAST_ID, broadcastId);
    }

    protected long getBroadcastId() {
        return getArguments().getLong(EXTRA_BROADCAST_ID);
    }
}
