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

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/5.
 */

public class ArticleListResult implements Parcelable{
    @SerializedName("errno")
    public int errno;

    public String errmsg;

    @SerializedName("data")
    public ParamsData paramsData;

    protected ArticleListResult(Parcel in) {
        errno = in.readInt();
        errmsg = in.readString();
        paramsData = in.readParcelable(ParamsData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errno);
        dest.writeString(errmsg);
        dest.writeParcelable(paramsData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArticleListResult> CREATOR = new Creator<ArticleListResult>() {
        @Override
        public ArticleListResult createFromParcel(Parcel in) {
            return new ArticleListResult(in);
        }

        @Override
        public ArticleListResult[] newArray(int size) {
            return new ArticleListResult[size];
        }
    };
}
