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

public class SizedImageItem implements Parcelable {

    public int height;

    public String url;

    public int width;


    public static final Parcelable.Creator<SizedImageItem> CREATOR =
            new Parcelable.Creator<SizedImageItem>() {
                @Override
                public SizedImageItem createFromParcel(Parcel source) {
                    return new SizedImageItem(source);
                }
                @Override
                public SizedImageItem[] newArray(int size) {
                    return new SizedImageItem[size];
                }
            };

    public SizedImageItem() {}

    protected SizedImageItem(Parcel in) {
        height = in.readInt();
        url = in.readString();
        width = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeString(url);
        dest.writeInt(width);
    }
}
