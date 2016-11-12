package me.zhanghai.android.douya.network.api.info.acapi;

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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(msg);
        dest.writeInt(status);
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
