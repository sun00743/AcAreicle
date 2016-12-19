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
