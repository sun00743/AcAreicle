package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/30.
 */

public class GetBanana implements Parcelable{

    public int bananaGain;
    /**
     * 金香蕉
     */
    public int bananaGold;
    public int bananaCost;
    /**
     * 香蕉
     */
    public int bananaCount;

    protected GetBanana(Parcel in) {
        bananaGain = in.readInt();
        bananaGold = in.readInt();
        bananaCost = in.readInt();
        bananaCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bananaGain);
        dest.writeInt(bananaGold);
        dest.writeInt(bananaCost);
        dest.writeInt(bananaCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetBanana> CREATOR = new Creator<GetBanana>() {
        @Override
        public GetBanana createFromParcel(Parcel in) {
            return new GetBanana(in);
        }

        @Override
        public GetBanana[] newArray(int size) {
            return new GetBanana[size];
        }
    };
}
