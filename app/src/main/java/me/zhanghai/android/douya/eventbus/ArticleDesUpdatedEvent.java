package me.zhanghai.android.douya.eventbus;

import me.zhanghai.android.douya.network.api.info.acapi.ArticleList;

/**
 * Created by Administrator on 2016/9/6.
 */

public class ArticleDesUpdatedEvent extends Event {

    public ArticleList articleDes;

    public ArticleDesUpdatedEvent(ArticleList articleDes, Object source) {
        super(source);

        this.articleDes = articleDes;
    }
}
