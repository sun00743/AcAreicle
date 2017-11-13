/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.acapi;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/26
 */
@Entity(indices = {@Index(value = {"userId", "user_name"}, unique = true)})
public class Acer implements Parcelable {

    @ColumnInfo(name = "access_token")
    public String access_token;

    @ColumnInfo(name = "user_img")
    public String userImg;

    @ColumnInfo(name = "expires")
    public Long expires;

    @ColumnInfo(name = "user_group_level")
    public int userGroupLevel;

    @ColumnInfo(name = "mobile_check")
    public int mobileCheck;

    /**
     * user id
     */
    @PrimaryKey
//            (autoGenerate = true)  //自动填充id
    public int userId;

    @ColumnInfo(name = "user_name")
    public String username;

    @ColumnInfo(name = "time")
    public int time;

//    @Ignore
//    public Bitmap bitmap

    public Acer() {
    }

    protected Acer(Parcel in) {
        access_token = in.readString();
        userImg = in.readString();
        userGroupLevel = in.readInt();
        mobileCheck = in.readInt();
        userId = in.readInt();
        username = in.readString();
        time = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(userImg);
        dest.writeInt(userGroupLevel);
        dest.writeInt(mobileCheck);
        dest.writeInt(userId);
        dest.writeString(username);
        dest.writeInt(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Acer> CREATOR = new Creator<Acer>() {
        @Override
        public Acer createFromParcel(Parcel in) {
            return new Acer(in);
        }

        @Override
        public Acer[] newArray(int size) {
            return new Acer[size];
        }
    };
}
