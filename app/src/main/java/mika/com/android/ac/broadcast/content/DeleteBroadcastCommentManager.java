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

public class DeleteBroadcastCommentManager
        extends ResourceWriterManager<DeleteBroadcastCommentWriter> {

    private static class InstanceHolder {
        public static final DeleteBroadcastCommentManager VALUE =
                new DeleteBroadcastCommentManager();
    }

    public static DeleteBroadcastCommentManager getInstance() {
        return InstanceHolder.VALUE;
    }

    public void write(long broadcastId, long commentId, Context context) {
        add(new DeleteBroadcastCommentWriter(broadcastId, commentId, this), context);
    }

    public boolean isWriting(long broadcastId, long commentId) {
        return findWriter(broadcastId, commentId) != null;
    }

    private DeleteBroadcastCommentWriter findWriter(long broadcastId, long commentId) {
        for (DeleteBroadcastCommentWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId && writer.getCommentId() == commentId) {
                return writer;
            }
        }
        return null;
    }
}
