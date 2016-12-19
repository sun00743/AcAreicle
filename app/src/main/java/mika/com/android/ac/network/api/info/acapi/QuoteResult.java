package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mika on 2016/12/15.
 */

public class QuoteResult implements Parcelable{
    public boolean success;
    public String msg;
    public int status;
    public QuoteData data;

    protected QuoteResult(Parcel in) {
        success = in.readByte() != 0;
        msg = in.readString();
        status = in.readInt();
        data = in.readParcelable(QuoteData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(msg);
        dest.writeInt(status);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuoteResult> CREATOR = new Creator<QuoteResult>() {
        @Override
        public QuoteResult createFromParcel(Parcel in) {
            return new QuoteResult(in);
        }

        @Override
        public QuoteResult[] newArray(int size) {
            return new QuoteResult[size];
        }
    };
}
