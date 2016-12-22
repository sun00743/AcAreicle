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
 * Created by Administrator on 2016/9/12.
 */

public class CommentResult implements Parcelable{
    public boolean success;
    public String msg;
    public int status;
    public CommentData data;

    protected CommentResult(Parcel in) {
        success = in.readByte() != 0;
        msg = in.readString();
        status = in.readInt();
        data = in.readParcelable(CommentData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(msg);
        dest.writeInt(status);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentResult> CREATOR = new Creator<CommentResult>() {
        @Override
        public CommentResult createFromParcel(Parcel in) {
            return new CommentResult(in);
        }

        @Override
        public CommentResult[] newArray(int size) {
            return new CommentResult[size];
        }
    };

    public CommentData getData() {
        return data;
    }
}
