package me.zhanghai.android.douya.eventbus;

import me.zhanghai.android.douya.network.api.info.acapi.Comment;

/**
 * Created by Administrator on 2016/9/18.
 */

public class CommentUpdatedEvent extends Event {

    public Comment comment;

    public CommentUpdatedEvent(Comment comment, Object source) {
        super(source);

        this.comment = comment;
    }
}
