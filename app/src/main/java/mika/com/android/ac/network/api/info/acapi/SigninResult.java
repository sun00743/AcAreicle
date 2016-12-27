/*
 * Copyright (c) 2016. Created by mingkai 16-12-27 上午10:01
 */

package mika.com.android.ac.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mika on 2016/12/27.
 */

public class SignInResult implements Parcelable{
    public String message;

    protected SignInResult(Parcel in) {
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignInResult> CREATOR = new Creator<SignInResult>() {
        @Override
        public SignInResult createFromParcel(Parcel in) {
            return new SignInResult(in);
        }

        @Override
        public SignInResult[] newArray(int size) {
            return new SignInResult[size];
        }
    };
}
