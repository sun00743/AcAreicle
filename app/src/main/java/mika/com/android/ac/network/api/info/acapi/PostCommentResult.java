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
 * Created by Administrator on 2016/11/1.
 */

public class PostCommentResult implements Parcelable{
    public boolean captcha;
    public String commentId;
    public int status;
    public boolean success;

    protected PostCommentResult(Parcel in) {
        captcha = in.readByte() != 0;
        commentId = in.readString();
        status = in.readInt();
        success = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (captcha ? 1 : 0));
        dest.writeString(commentId);
        dest.writeInt(status);
        dest.writeByte((byte) (success ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostCommentResult> CREATOR = new Creator<PostCommentResult>() {
        @Override
        public PostCommentResult createFromParcel(Parcel in) {
            return new PostCommentResult(in);
        }

        @Override
        public PostCommentResult[] newArray(int size) {
            return new PostCommentResult[size];
        }
    };
}
