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

public class SendBroadcastCommentManager extends ResourceWriterManager<SendBroadcastCommentWriter> {

    private static class InstanceHolder {
        public static final SendBroadcastCommentManager VALUE = new SendBroadcastCommentManager();
    }

    public static SendBroadcastCommentManager getInstance() {
        return InstanceHolder.VALUE;
    }

    public void write(long broadcastId, String comment, Context context) {
        add(new SendBroadcastCommentWriter(broadcastId, comment, this), context);
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    public String getComment(long broadcastId) {
        SendBroadcastCommentWriter writer = findWriter(broadcastId);
        return writer != null ? writer.getComment() : null;
    }

    private SendBroadcastCommentWriter findWriter(long broadcastId) {
        for (SendBroadcastCommentWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
