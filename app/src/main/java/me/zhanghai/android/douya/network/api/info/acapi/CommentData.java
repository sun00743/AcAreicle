package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CommentData implements Parcelable{
    public String cache;
    public CommentPage page;

    protected CommentData(Parcel in) {
        cache = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cache);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentData> CREATOR = new Creator<CommentData>() {
        @Override
        public CommentData createFromParcel(Parcel in) {
            return new CommentData(in);
        }

        @Override
        public CommentData[] newArray(int size) {
            return new CommentData[size];
        }
    };

    public CommentPage getPage() {
        return page;
    }

    public void setPage(CommentPage page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "cache='" + cache + '\'' +
                ", page=" + page +
                '}';
    }
}
