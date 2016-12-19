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
 * Created by Administrator on 2016/9/26.
 */

public class AcerInfoResult implements Parcelable{
    public boolean success;

    public String msg;

    public int status;

    public AcerInfoData data;

    protected AcerInfoResult(Parcel in) {
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

    public static final Creator<AcerInfoResult> CREATOR = new Creator<AcerInfoResult>() {
        @Override
        public AcerInfoResult createFromParcel(Parcel in) {
            return new AcerInfoResult(in);
        }

        @Override
        public AcerInfoResult[] newArray(int size) {
            return new AcerInfoResult[size];
        }
    };
}
