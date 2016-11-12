package me.zhanghai.android.douya.network.api.info.acapi;

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
     * 投稿收藏？
     */
    public int stows;
    public int comments;
    /**
     * 性别
     */
    public int gender;
    public int currExp;
    public int nextLevelNeed;
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
