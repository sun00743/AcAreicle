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

public class DeleteBroadcastManager extends ResourceWriterManager<DeleteBroadcastWriter> {

    private static class InstanceHolder {
        public static final DeleteBroadcastManager VALUE = new DeleteBroadcastManager();
    }

    public static DeleteBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    public void write(long broadcastId, Context context) {
        add(new DeleteBroadcastWriter(broadcastId, this), context);
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    private DeleteBroadcastWriter findWriter(long broadcastId) {
        for (DeleteBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
