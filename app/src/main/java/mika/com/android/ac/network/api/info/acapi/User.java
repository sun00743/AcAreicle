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

public class User implements Parcelable {
    public int id;
    public String name;
    public String avatar;
    public String signature;
    public long savedTime;
    public boolean isExpired(){
        return System.currentTimeMillis() - savedTime >= 14 * 14 * 60*60*24*1000;
    }
    public User(){}
    public User(int id, String name, String avatar, String signature) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.signature = signature;
    }
    public User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        avatar = in.readString();
        signature = in.readString();
        cookies = in.readString();
        savedTime = in.readLong();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(signature);
        dest.writeString(cookies);
        dest.writeLong(savedTime);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
    public String cookies;
    
}
