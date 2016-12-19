package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mika on 2016/12/15.
 */

public class QuoteData implements Parcelable{
    public QuotePage page;

    protected QuoteData(Parcel in) {
        page = in.readParcelable(QuotePage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(page, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuoteData> CREATOR = new Creator<QuoteData>() {
        @Override
        public QuoteData createFromParcel(Parcel in) {
            return new QuoteData(in);
        }

        @Override
        public QuoteData[] newArray(int size) {
            return new QuoteData[size];
        }
    };
}
