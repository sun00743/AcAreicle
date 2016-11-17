/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.content.Context;

import mika.com.android.ac.content.ResourceWriterManager;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.util.ToastUtil;

public class RebroadcastBroadcastManager extends ResourceWriterManager<RebroadcastBroadcastWriter> {

    private static class InstanceHolder {
        public static final RebroadcastBroadcastManager VALUE = new RebroadcastBroadcastManager();
    }

    public static RebroadcastBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(Broadcast, boolean, Context)} instead.
     */
    public void write(long broadcastId, boolean rebroadcast, Context context) {
        add(new RebroadcastBroadcastWriter(broadcastId, rebroadcast, this), context);
    }

    public boolean write(Broadcast broadcast, boolean rebroadcast, Context context) {
        if (shouldWrite(broadcast, context)) {
            add(new RebroadcastBroadcastWriter(broadcast, rebroadcast, this), context);
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldWrite(Broadcast broadcast, Context context) {
        if (broadcast.isAuthorOneself(context)) {
            ToastUtil.show(mika.com.android.ac.R.string.broadcast_rebroadcast_error_cannot_rebroadcast_oneself,
                    context);
            return false;
        } else {
            return true;
        }
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    public boolean isWritingRebroadcast(long broadcastId) {
        RebroadcastBroadcastWriter writer = findWriter(broadcastId);
        return writer != null && writer.isRebroadcast();
    }

    private RebroadcastBroadcastWriter findWriter(long broadcastId) {
        for (RebroadcastBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
