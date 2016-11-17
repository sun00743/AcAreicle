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

public class LikeBroadcastManager extends ResourceWriterManager<LikeBroadcastWriter> {

    private static class InstanceHolder {
        public static final LikeBroadcastManager VALUE = new LikeBroadcastManager();
    }

    public static LikeBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(Broadcast, boolean, Context)} instead.
     */
    public void write(long broadcastId, boolean like, Context context) {
        add(new LikeBroadcastWriter(broadcastId, like, this), context);
    }

    public boolean write(Broadcast broadcast, boolean like, Context context) {
        if (shouldWrite(broadcast, context)) {
            add(new LikeBroadcastWriter(broadcast, like, this), context);
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldWrite(Broadcast broadcast, Context context) {
        if (broadcast.isAuthorOneself(context)) {
            ToastUtil.show(mika.com.android.ac.R.string.broadcast_like_error_cannot_like_oneself, context);
            return false;
        } else {
            return true;
        }
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    public boolean isWritingLike(long broadcastId) {
        LikeBroadcastWriter writer = findWriter(broadcastId);
        return writer != null && writer.isLike();
    }

    private LikeBroadcastWriter findWriter(long broadcastId) {
        for (LikeBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
