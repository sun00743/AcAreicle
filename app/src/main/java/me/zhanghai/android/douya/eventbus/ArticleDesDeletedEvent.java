/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.eventbus;

public class ArticleDesDeletedEvent extends Event {

    public long articleDesId;

    public ArticleDesDeletedEvent(long articleDesId, Object source) {
        super(source);

        this.articleDesId = articleDesId;
    }
}
