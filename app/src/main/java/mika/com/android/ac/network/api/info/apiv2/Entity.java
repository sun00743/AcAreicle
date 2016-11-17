/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api.info.apiv2;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.List;

import mika.com.android.ac.settings.info.Settings;
import mika.com.android.ac.ui.UriSpan;
import mika.com.android.ac.util.LogUtils;

public class Entity implements Parcelable {

    public int end;

    public String href;

    public int start;

    public String title;

    public static CharSequence applyEntities(String text, List<Entity> entityList,
                                             Context context) {

        if (TextUtils.isEmpty(text) || entityList.isEmpty()) {
            return text;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int lastTextIndex = 0;
        for (Entity entity : entityList) {
            if (entity.start < 0 || entity.end < entity.start) {
                LogUtils.w("Ignoring malformed entity " + entity);
                continue;
            }
            if (entity.start < lastTextIndex) {
                LogUtils.w("Ignoring backward entity " + entity + ", with lastTextIndex="
                        + lastTextIndex);
                continue;
            }
            int entityStart = text.offsetByCodePoints(lastTextIndex, entity.start - lastTextIndex);
            int entityEnd = text.offsetByCodePoints(entityStart, entity.end - entity.start);
            builder.append(text.subSequence(lastTextIndex, entityStart));
            CharSequence entityTitle = entity.title;
            if (!Settings.SHOW_TITLE_FOR_LINK_ENTITY.getValue(context)
                    && Patterns.WEB_URL.matcher(entityTitle).matches()) {
                entityTitle = text.subSequence(entityStart, entityEnd);
            }
            int entityStartInAppliedText = builder.length();
            builder
                    .append(entityTitle)
                    .setSpan(new UriSpan(entity.href), entityStartInAppliedText,
                            entityStartInAppliedText + entityTitle.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            lastTextIndex = entityEnd;
        }
        if (lastTextIndex != text.length()) {
            builder.append(text.subSequence(lastTextIndex, text.length()));
        }
        return builder;
    }


    public static final Parcelable.Creator<Entity> CREATOR = new Parcelable.Creator<Entity>() {
        public Entity createFromParcel(Parcel source) {
            return new Entity(source);
        }
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    public Entity() {}

    protected Entity(Parcel in) {
        end = in.readInt();
        href = in.readString();
        start = in.readInt();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(end);
        dest.writeString(href);
        dest.writeInt(start);
        dest.writeString(title);
    }

}
