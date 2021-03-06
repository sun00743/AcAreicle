/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.frodo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    public enum Type {

        APP("app", mika.com.android.ac.R.string.item_app_name, mika.com.android.ac.R.string.item_app_action),
        BOOK("book", mika.com.android.ac.R.string.item_book_name, mika.com.android.ac.R.string.item_book_action),
        EVENT("event", mika.com.android.ac.R.string.item_event_name, mika.com.android.ac.R.string.item_event_action),
        GAME("game", mika.com.android.ac.R.string.item_game_name, mika.com.android.ac.R.string.item_game_action),
        MOVIE("movie", mika.com.android.ac.R.string.item_movie_name, mika.com.android.ac.R.string.item_movie_action),
        MUSIC("music", mika.com.android.ac.R.string.item_music_name, mika.com.android.ac.R.string.item_music_action);

        private String apiString;
        private int nameRes;
        private int actionRes;

        Type(String apiString, int nameRes, int actionRes) {
            this.apiString = apiString;
            this.nameRes = nameRes;
            this.actionRes = actionRes;
        }

        public static Type ofString(String apiString, Type defaultValue) {
            for (Type type : Type.values()) {
                if (TextUtils.equals(type.apiString, apiString)) {
                    return type;
                }
            }
            return defaultValue;
        }

        public static Type ofString(String apiString) {
            return ofString(apiString, BOOK);
        }

        /**
         * @deprecated HACK-only.
         */
        public String getApiString() {
            return apiString;
        }

        public int getNameRes() {
            return nameRes;
        }

        public String getName(Context context) {
            return context.getString(nameRes);
        }

        public int getActionRes() {
            return actionRes;
        }

        public String getAction(Context context) {
            return context.getString(actionRes);
        }
    }

    public long id;

    @SerializedName("pic")
    public Image cover;

    public Rating rating;

    @SerializedName("sharing_url")
    public String shareUrl;

    public String title;

    /**
     * @deprecated Use {@link #getType()} instead.
     */
    public String type;

    public String uri;

    public String url;

    public Type getType() {
        //noinspection deprecation
        return Type.ofString(type);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }
        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Item() {}

    protected Item(Parcel in) {
        id = in.readLong();
        cover = in.readParcelable(Image.class.getClassLoader());
        rating = in.readParcelable(Rating.class.getClassLoader());
        shareUrl = in.readString();
        title = in.readString();
        //noinspection deprecation
        type = in.readString();
        uri = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(cover, flags);
        dest.writeParcelable(rating, flags);
        dest.writeString(shareUrl);
        dest.writeString(title);
        //noinspection deprecation
        dest.writeString(type);
        dest.writeString(uri);
        dest.writeString(url);
    }
}
