package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/26.
 */

public class AcerInfoData implements Parcelable{
    public AcerInfo fullUser;

    protected AcerInfoData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AcerInfoData> CREATOR = new Creator<AcerInfoData>() {
        @Override
        public AcerInfoData createFromParcel(Parcel in) {
            return new AcerInfoData(in);
        }

        @Override
        public AcerInfoData[] newArray(int size) {
            return new AcerInfoData[size];
        }
    };
}
