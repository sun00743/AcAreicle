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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Comment implements Parcelable {
//    private static final long serialVersionUID = 1L;
    public int id;
    public int quoteId;
    public int refCount;
    public String content;
    public long time;
    public int userId;
    public String username;
    public String avatar;
    public int floor;
    public int deep;
    public int isAt;
    public int nameRed;
    public int avatarFrame;
    public boolean isQuoted;
    public boolean isDelete;
    public boolean isUpDelete;

    public Comment(){}

    protected Comment(Parcel in) {
        id = in.readInt();
        quoteId = in.readInt();
        refCount = in.readInt();
        content = in.readString();
        time = in.readLong();
        userId = in.readInt();
        username = in.readString();
        avatar = in.readString();
        floor = in.readInt();
        deep = in.readInt();
        isAt = in.readInt();
        nameRed = in.readInt();
        avatarFrame = in.readInt();
        isQuoted = in.readByte() != 0;
        isDelete = in.readByte() != 0;
        isUpDelete = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(quoteId);
        dest.writeInt(refCount);
        dest.writeString(content);
        dest.writeLong(time);
        dest.writeInt(userId);
        dest.writeString(username);
        dest.writeString(avatar);
        dest.writeInt(floor);
        dest.writeInt(deep);
        dest.writeInt(isAt);
        dest.writeInt(nameRed);
        dest.writeInt(avatarFrame);
        dest.writeByte((byte) (isQuoted ? 1 : 0));
        dest.writeByte((byte) (isDelete ? 1 : 0));
        dest.writeByte((byte) (isUpDelete ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public void setContent(String content) {
        this.content = content;
    }

    public String calculateTimeDiff(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        diff = new Date(System.currentTimeMillis()).getTime()
                - this.time;
        long days = (diff/(1000*60*60*24));
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        String timeDiff;
        if (days <= 0){
            if(hours <= 0){
                timeDiff = minutes + " 分钟前";
            }else{
                timeDiff = hours + " 小时前";
            }
        }else{
            timeDiff = days + " 天前";
        }
        return timeDiff;
    }
}
