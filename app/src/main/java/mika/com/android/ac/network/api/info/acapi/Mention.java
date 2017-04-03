package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mika on 2016/12/20
 *
 * "@"
 */

public class Mention implements Parcelable {
    public int[] special;
    public int newPush;
    public int newFollowed;
    public boolean success;
    public int[] bangumi;
    public int unReadMail;
    public int mention;
    public int setting;

    protected Mention(Parcel in) {
        special = in.createIntArray();
        newPush = in.readInt();
        newFollowed = in.readInt();
        success = in.readByte() != 0;
        bangumi = in.createIntArray();
        unReadMail = in.readInt();
        mention = in.readInt();
        setting = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(special);
        dest.writeInt(newPush);
        dest.writeInt(newFollowed);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeIntArray(bangumi);
        dest.writeInt(unReadMail);
        dest.writeInt(mention);
        dest.writeInt(setting);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mention> CREATOR = new Creator<Mention>() {
        @Override
        public Mention createFromParcel(Parcel in) {
            return new Mention(in);
        }

        @Override
        public Mention[] newArray(int size) {
            return new Mention[size];
        }
    };
}
