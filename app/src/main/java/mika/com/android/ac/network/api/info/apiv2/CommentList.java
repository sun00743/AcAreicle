/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.apiv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CommentList extends BaseList implements Parcelable {

    public ArrayList<Comment> comments = new ArrayList<>();


    public static final Parcelable.Creator<CommentList> CREATOR =
            new Parcelable.Creator<CommentList>() {
                public CommentList createFromParcel(Parcel source) {
                    return new CommentList(source);
                }
                public CommentList[] newArray(int size) {
                    return new CommentList[size];
                }
            };

    public CommentList() {}

    protected CommentList(Parcel in) {
        super(in);

        this.comments = in.createTypedArrayList(Comment.CREATOR);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeTypedList(comments);
    }
}
