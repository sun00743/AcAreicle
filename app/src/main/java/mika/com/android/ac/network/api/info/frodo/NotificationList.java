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

import java.util.ArrayList;

public class NotificationList implements Parcelable {

    public int count;

    public ArrayList<Notification> notifications = new ArrayList<>();

    public int start;


    public static final Creator<NotificationList> CREATOR = new Creator<NotificationList>() {

        public NotificationList createFromParcel(Parcel source) {
            return new NotificationList(source);
        }

        public NotificationList[] newArray(int size) {
            return new NotificationList[size];
        }
    };

    public NotificationList() {}

    protected NotificationList(Parcel in) {
        count = in.readInt();
        notifications = in.createTypedArrayList(Notification.CREATOR);
        start = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeTypedList(notifications);
        dest.writeInt(start);
    }
}
