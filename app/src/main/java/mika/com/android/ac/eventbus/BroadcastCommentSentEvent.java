/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.eventbus;

import mika.com.android.ac.network.api.info.apiv2.Comment;

public class BroadcastCommentSentEvent extends Event {

    public long broadcastId;

    public Comment comment;

    public BroadcastCommentSentEvent(long broadcastId, Comment comment, Object source) {
        super(source);

        this.broadcastId = broadcastId;
        this.comment = comment;
    }
}
