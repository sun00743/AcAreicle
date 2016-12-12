/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/27.
 * "userjson":{
 "currExp":3558,
 "stows":312,
 "comments":59,
 "gender":1,
 "level":59,
 "sign":"这个人很撒比，什么都瞎写...",
 "follows":0,
 "lastLoginDate":"2016-09-26 23:07:20.0",
 "avatar":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201609/25164557vbmqj58j.jpg",
 "posts":1,
 "followed":0,
 "lastLoginIp":"125.71.161.*",
 "fans":0,
 "uid":623674,
 "regTime":"2013-08-11 23:45:15.0",
 "nextLevelNeed":3800,
 "comeFrom":"山东,不限",
 "name":"月与萧",
 "dTime":"",
 "expPercent":59,
 "isFriend":0,
 "views":8370
 },
 */

public class AcerInfo2 implements Parcelable{
    public String name;
    /**
     * 个性签名
     */
    public String sign;
    public String avatar;
    public int fans;
    /**
     * 当前所有有效投稿的收藏数总和。
     */
    public int stows;
    /**
     * 当前所有有效投稿的评论数总和。
     */
    public int comments;
    /**
     * 性别
     */
    public int gender;
    public int currExp;
    public int nextLevelNeed;
    /**
     * 当前所有有效投稿的浏览数总和。
     */
    public int views;
    public int isFriend;
    /**
     * 投稿数
     */
    public int posts;
    /**
     * 注册时间
     */
    public String regTime;
    public int uid;
    public int expPercent;
    public int level;

    public AcerInfo2(){}

    protected AcerInfo2(Parcel in) {
        name = in.readString();
        sign = in.readString();
        avatar = in.readString();
        fans = in.readInt();
        stows = in.readInt();
        comments = in.readInt();
        gender = in.readInt();
        currExp = in.readInt();
        nextLevelNeed = in.readInt();
        views = in.readInt();
        isFriend = in.readInt();
        posts = in.readInt();
        regTime = in.readString();
        uid = in.readInt();
        expPercent = in.readInt();
        level = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(sign);
        dest.writeString(avatar);
        dest.writeInt(fans);
        dest.writeInt(stows);
        dest.writeInt(comments);
        dest.writeInt(gender);
        dest.writeInt(currExp);
        dest.writeInt(nextLevelNeed);
        dest.writeInt(views);
        dest.writeInt(isFriend);
        dest.writeInt(posts);
        dest.writeString(regTime);
        dest.writeInt(uid);
        dest.writeInt(expPercent);
        dest.writeInt(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AcerInfo2> CREATOR = new Creator<AcerInfo2>() {
        @Override
        public AcerInfo2 createFromParcel(Parcel in) {
            return new AcerInfo2(in);
        }

        @Override
        public AcerInfo2[] newArray(int size) {
            return new AcerInfo2[size];
        }
    };
}
