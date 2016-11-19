/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.frodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Notification implements Parcelable {

    public long id;

    @SerializedName(value = "is_read")
    public boolean read;

    @SerializedName(value = "target_uri")
    public String targetUri;

    public String text;

    public String time;

    private void fixText() {
        int index = text.lastIndexOf("回复");
        if (index == text.length() - "回复".length()) {
            text = text.substring(0, index) + "回应";
        }
    }

    private void fixTime() {
        if (time.startsWith("\"") && time.endsWith("\"")) {
            time = time.substring(1, time.length() - 1);
        }
    }

    public void fix() {
        fixText();
        fixTime();
    }


    public static final Creator<Notification> CREATOR = new Creator<Notification>() {

        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public Notification() {}

    protected Notification(Parcel in) {
        id = in.readLong();
        read = in.readByte() != 0;
        targetUri = in.readString();
        text = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeByte(read ? (byte) 1 : (byte) 0);
        dest.writeString(targetUri);
        dest.writeString(text);
        dest.writeString(time);
    }
}