/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.eventbus;

public class ArticleDesWriteFinishedEvent extends Event {

    public long articleDesId;

    public ArticleDesWriteFinishedEvent(long articleDesId, Object source) {
        super(source);

        this.articleDesId = articleDesId;
    }
}
