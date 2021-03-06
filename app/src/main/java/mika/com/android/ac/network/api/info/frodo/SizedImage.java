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

public class SizedImage implements Parcelable {

    @SerializedName("is_animated")
    public boolean isAnimated;

    /**
     * @deprecated Use {@link #getLarge()} instead.
     */
    public SizedImageItem large;

    /**
     * @deprecated Use {@link #getNormal()} instead.
     */
    public SizedImageItem normal;

    /**
     * @deprecated Use {@link #getSmall()} instead.
     */
    public SizedImageItem small;

    public SizedImageItem getLarge() {
        //noinspection deprecation
        return large != null ? large
                : normal != null ? normal
                : small;
    }

    public SizedImageItem getNormal() {
        //noinspection deprecation
        return normal != null ? normal
                : large != null ? large
                : small;
    }

    private SizedImageItem getSmall() {
        //noinspection deprecation
        return small != null ? small
                : normal != null ? normal
                : large;
    }


    public static final Parcelable.Creator<SizedImage> CREATOR =
            new Parcelable.Creator<SizedImage>() {
                @Override
                public SizedImage createFromParcel(Parcel source) {
                    return new SizedImage(source);
                }
                @Override
                public SizedImage[] newArray(int size) {
                    return new SizedImage[size];
                }
            };

    public SizedImage() {}

    protected SizedImage(Parcel in) {
        isAnimated = in.readByte() != 0;
        //noinspection deprecation
        large = in.readParcelable(SizedImageItem.class.getClassLoader());
        //noinspection deprecation
        normal = in.readParcelable(SizedImageItem.class.getClassLoader());
        //noinspection deprecation
        small = in.readParcelable(SizedImageItem.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isAnimated ? (byte) 1 : (byte) 0);
        //noinspection deprecation
        dest.writeParcelable(large, flags);
        //noinspection deprecation
        dest.writeParcelable(normal, flags);
        //noinspection deprecation
        dest.writeParcelable(small, flags);
    }
}
