package mika.com.android.ac.article.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Locale;

import mika.com.android.ac.ui.EmotionView;

/**
 * Created by SunMingKai on 2016/12/5.
 * <p>
 * emoticon adapter
 */

public class EmoticonGridAdapter extends BaseAdapter {

    private static final String[] TYPE = {"ac", "ais", "ac2", "ac3", "td", "brd"};
    private static final int[] COUNT = {54, 40, 55, 16, 40, 40};
    private Activity activity;
    private int type;

    public EmoticonGridAdapter(Activity activity, int type) {
        this.type = type;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new EmotionView(activity.getApplicationContext());
        }
        ((EmotionView) convertView).setEmotionId(position + 1, type);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        int id;
        id = position + 1;
        return String.format(Locale.US, "[emot=%s,%02d/]", TYPE[type], id);


//        String cat;
//        int id;
//        if (position < 54) {
//            cat = "ac";
//            id = position + 1;
//        } else {
//            cat = position >= 94 ? "ac2" : "ac";
//            id = position >= 94 ? position - 93 : position - 53;
//        }
//        return String.format(Locale.US, "[emot=%s,%02d/]", cat, id);
    }

    @Override
    public int getCount() {
        return COUNT[type];
    }
}

