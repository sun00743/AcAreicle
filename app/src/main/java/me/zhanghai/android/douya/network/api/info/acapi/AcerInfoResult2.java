package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/27.
 */

public class AcerInfoResult2 implements Parcelable{
    public boolean success;
    public AcerInfo2 userjson;

    protected AcerInfoResult2(Parcel in) {
        success = in.readByte() != 0;
        userjson = in.readParcelable(AcerInfo2.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeParcelable(userjson, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AcerInfoResult2> CREATOR = new Creator<AcerInfoResult2>() {
        @Override
        public AcerInfoResult2 createFromParcel(Parcel in) {
            return new AcerInfoResult2(in);
        }

        @Override
        public AcerInfoResult2[] newArray(int size) {
            return new AcerInfoResult2[size];
        }
    };
}
