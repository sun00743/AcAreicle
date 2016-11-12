package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ParamsData implements Parcelable{
//    @SerializedName("params")
//    public RequestParams params;

    @SerializedName("data")
    public ArrayList<ArticleList> articleLists = new ArrayList<>();

    protected ParamsData(Parcel in) {
        articleLists = in.createTypedArrayList(ArticleList.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(articleLists);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParamsData> CREATOR = new Creator<ParamsData>() {
        @Override
        public ParamsData createFromParcel(Parcel in) {
            return new ParamsData(in);
        }

        @Override
        public ParamsData[] newArray(int size) {
            return new ParamsData[size];
        }
    };
}
