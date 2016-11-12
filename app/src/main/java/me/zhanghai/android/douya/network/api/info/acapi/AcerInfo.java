package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/26.
 *
 "currExp":3200,
 "nextLevelNeed":3800,
 "banana":116,
 "bananaGold":0,
 "signature":"这个人很撒比，什么都瞎写...",
 "level":17,
 "gender":1,
 "exp":3548,
 "mobile":"183****3769",
 "mobileCheck":1,
 "lastLoginDate":0,
 "contributes":0,
 "qq":"327400482",
 "followed":0,
 "following":0,
 "userId":623674,
 "username":"月与萧",
 "userImg":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201609/25164557vbmqj58j.jpg"
 *
 */

public class AcerInfo implements Parcelable{
    public int userImg;
    public int username;
    public int userId;
    public int following;
    public int followed;
    public int qq;
    /**
     * 丢的香蕉？
     */
    public int contributes;
    public int lastLoginDate;
    public int mobileCheck;
    /**
     * 电话号码
     */
    public int mobile;
    public int exp;
    /**
     * 性别
     */
    public int gender;
    public int level;
    public int bananaGold;
    public int signature;
    public int banana;
    public int nextLevelNeed;
    public int currExp;

    protected AcerInfo(Parcel in) {
        userImg = in.readInt();
        username = in.readInt();
        userId = in.readInt();
        following = in.readInt();
        followed = in.readInt();
        qq = in.readInt();
        contributes = in.readInt();
        lastLoginDate = in.readInt();
        mobileCheck = in.readInt();
        mobile = in.readInt();
        exp = in.readInt();
        gender = in.readInt();
        level = in.readInt();
        bananaGold = in.readInt();
        signature = in.readInt();
        banana = in.readInt();
        nextLevelNeed = in.readInt();
        currExp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userImg);
        dest.writeInt(username);
        dest.writeInt(userId);
        dest.writeInt(following);
        dest.writeInt(followed);
        dest.writeInt(qq);
        dest.writeInt(contributes);
        dest.writeInt(lastLoginDate);
        dest.writeInt(mobileCheck);
        dest.writeInt(mobile);
        dest.writeInt(exp);
        dest.writeInt(gender);
        dest.writeInt(level);
        dest.writeInt(bananaGold);
        dest.writeInt(signature);
        dest.writeInt(banana);
        dest.writeInt(nextLevelNeed);
        dest.writeInt(currExp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AcerInfo> CREATOR = new Creator<AcerInfo>() {
        @Override
        public AcerInfo createFromParcel(Parcel in) {
            return new AcerInfo(in);
        }

        @Override
        public AcerInfo[] newArray(int size) {
            return new AcerInfo[size];
        }
    };
}
