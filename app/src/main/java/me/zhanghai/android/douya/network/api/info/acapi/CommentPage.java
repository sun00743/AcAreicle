package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CommentPage implements Parcelable{
    public int totalCount;
    public int pageSize;
    public int pageNo;
    public int[] list;
//    public TypeToken<Map<String, Comment>> map;
//    public String map;
    public JsonElement map;

    protected CommentPage(Parcel in) {
        totalCount = in.readInt();
        pageSize = in.readInt();
        pageNo = in.readInt();
        list = in.createIntArray();
//        map = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeInt(pageSize);
        dest.writeInt(pageNo);
        dest.writeIntArray(list);
//        dest.writeString(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentPage> CREATOR = new Creator<CommentPage>() {
        @Override
        public CommentPage createFromParcel(Parcel in) {
            return new CommentPage(in);
        }

        @Override
        public CommentPage[] newArray(int size) {
            return new CommentPage[size];
        }
    };
}
