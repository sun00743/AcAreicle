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

public class SizedPhoto implements Parcelable {

    public String description;

    public long id;

    public SizedImage image;

    @SerializedName("tag_name")
    public String tag;


    public static final Parcelable.Creator<SizedPhoto> CREATOR =
            new Parcelable.Creator<SizedPhoto>() {
                @Override
                public SizedPhoto createFromParcel(Parcel source) {
                    return new SizedPhoto(source);
                }
                @Override
                public SizedPhoto[] newArray(int size) {
                    return new SizedPhoto[size];
                }
            };

    public SizedPhoto() {}

    protected SizedPhoto(Parcel in) {
        description = in.readString();
        id = in.readLong();
        image = in.readParcelable(SizedImage.class.getClassLoader());
        tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeLong(id);
        dest.writeParcelable(image, flags);
        dest.writeString(tag);
    }
}
