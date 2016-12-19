package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mika on 2016/12/15.
 */

public class QuotePage implements Parcelable{
    public int totalCount;
    public int pageSize;
    public int pageNo;
    public List<Comment> commentList;
    public List<Comment> quoteCommentList;

    protected QuotePage(Parcel in) {
        totalCount = in.readInt();
        pageSize = in.readInt();
        pageNo = in.readInt();
        commentList = in.createTypedArrayList(Comment.CREATOR);
        quoteCommentList = in.createTypedArrayList(Comment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
        dest.writeInt(pageSize);
        dest.writeInt(pageNo);
        dest.writeTypedList(commentList);
        dest.writeTypedList(quoteCommentList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuotePage> CREATOR = new Creator<QuotePage>() {
        @Override
        public QuotePage createFromParcel(Parcel in) {
            return new QuotePage(in);
        }

        @Override
        public QuotePage[] newArray(int size) {
            return new QuotePage[size];
        }
    };
}
