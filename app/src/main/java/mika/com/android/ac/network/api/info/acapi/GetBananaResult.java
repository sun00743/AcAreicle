package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/29.
 */

public class GetBananaResult implements Parcelable{

    public boolean success;
    public String result;
    public String info;
    public int status;
    public GetBanana data;

    protected GetBananaResult(Parcel in) {
        success = in.readByte() != 0;
        result = in.readString();
        info = in.readString();
        status = in.readInt();
        data = in.readParcelable(GetBanana.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(result);
        dest.writeString(info);
        dest.writeInt(status);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetBananaResult> CREATOR = new Creator<GetBananaResult>() {
        @Override
        public GetBananaResult createFromParcel(Parcel in) {
            return new GetBananaResult(in);
        }

        @Override
        public GetBananaResult[] newArray(int size) {
            return new GetBananaResult[size];
        }
    };
}
