/*
 * Copyright (c) 2016. Created by mingkai 16-12-27 下午3:53
 */

package mika.com.android.ac.articlelist.content;

/**
 * Created by mika on 2016/12/27.
 *
 */

public interface ListSort {
    interface Channels {
        /**
         * 综合
         */
        int HOME = 110;
        /**
         * 工作/感情
         */
        int GREEN = 73;
        /**
         * 动漫文化
         */
        int ANIMATION = 74;
        /**
         * 漫画/小说
         */
        int FICTION = 75;
        /**
         * 游戏
         */
        int GAME = 164;
    }

    interface Sort {
        /**
         * 最新发布
         */
        int NEWS = 0;
        /**
         * 最新回复
         */
        int REPLY = 5;
    }
}
