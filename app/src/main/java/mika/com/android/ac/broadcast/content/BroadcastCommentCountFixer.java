/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import java.util.List;

import mika.com.android.ac.eventbus.BroadcastUpdatedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.Comment;

public class BroadcastCommentCountFixer {

    private BroadcastCommentCountFixer() {}

    public static void onCommentRemoved(Broadcast broadcast, Object eventSource) {

        if (broadcast == null) {
            return;
        }

        --broadcast.commentCount;
        EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, eventSource));
    }

    public static void onCommentListChanged(Broadcast broadcast, List<Comment> commentList,
                                            Object eventSource) {

        if (broadcast == null || commentList == null) {
            return;
        }

        if (broadcast.commentCount < commentList.size()) {
            broadcast.commentCount = commentList.size();
            EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, eventSource));
        }
    }
}
