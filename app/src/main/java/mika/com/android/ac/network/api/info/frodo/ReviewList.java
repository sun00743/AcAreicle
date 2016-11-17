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

import java.util.ArrayList;

public class ReviewList extends BaseList {

    public ArrayList<Review> reviews = new ArrayList<>();


    public static final Creator<ReviewList> CREATOR = new Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel source) {
            return new ReviewList(source);
        }
        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };

    public ReviewList() {}

    protected ReviewList(Parcel in) {
        super(in);

        reviews = in.createTypedArrayList(Review.CREATOR);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeTypedList(reviews);
    }
}
