/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.acapi.Comment;

/**
 * Created by Administrator on 2016/9/20.
 */

public class TextViewUtils {

    public static void setCommentContent(final TextView textView, Comment comment) {
        if (textView.getMovementMethod() != null) // reset focus
            textView.setMovementMethod(null);
        String text = comment.content;
        if (TextUtils.isEmpty(text)) {
            textView.setText("");
            return;
        }
        text = replace(text);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new CommentImageGetter(textView), new CommentTagHandler()));
            } else {
                textView.setText(Html.fromHtml(text, new CommentImageGetter(textView), new CommentTagHandler()));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: text 的格式可能有问题
            textView.setText(text);
            Log.e("wtf", "set comment", e);
        }
//        comment.setTextColor(Color.BLACK);
//        textView.setTextSize();
        Pattern http = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]",
                Pattern.CASE_INSENSITIVE);
        Linkify.addLinks(textView, http, "http://");
        Linkify.addLinks(textView, Pattern.compile("(ac\\d{5,})", Pattern.CASE_INSENSITIVE), "ac://");
    }

    static void end(SpannableStringBuilder text, Class<?> kind,
                    Object repl) {
        int len = text.length();
        Object obj = getLast(text);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            text.setSpan(repl, where < 0 ? 0 : where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return;
    }

    public static Object getLast(Spanned text) {

        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        Object[] objs = text.getSpans(0, text.length(), Object.class);

        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    private static String replace(String text) {
        String reg = "\\[emot=(.*?),(.*?)\\/\\]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String id = m.group(2);
            String cat = m.group(1);
            int parsedId;
            try {
                parsedId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                // Invalid format text
                continue;
            }
//            if (parsedId > 54)
//                id = "54";
            String replace = cat.equals("brd") || cat.equals("td") || cat.equals("ac2") ?
                    "<img src='emotion/" + cat + "/%02d.gif'/>" : "<img src='emotion/%02d.gif'/>";
            text = text.replace(m.group(), String.format(replace, parsedId));
        }
        reg = "\\[at\\](.*?)\\[\\/at\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), "<font color=\"#FF9A03\" >@" + m.group(1) + "</font> ");
        }
        reg = "\\[color=(.*?)\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), "<font color=\"" + m.group(1) + "\" >");
        }
        text = text.replace("[/color]", "</font>");
        text = text.replaceAll("\\[size=(.*?)\\]", "").replace("[/size]", "");

        reg = "\\[img=(.*?)\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), m.group(1));
        }
        text = text.replace("[img]", "").replace("[/img]", "");
        text = text.replaceAll("\\[ac=\\d{5,}\\]", "").replace("[/ac]", "");
        text = text.replaceAll("\\[font[^\\]]*?\\]", "").replace("[/font]", "");
        text = text.replaceAll("\\[align[^\\]]*?\\]", "").replace("[/align]", "");
        text = text.replaceAll("\\[back[^\\]]*?\\]", "").replace("[/back]", "");
        text = text.replace("[s]", "<strike>").replace("[/s]", "</strike>");
        text = text.replace("[b]", "<b>").replace("[/b]", "</b>");
        text = text.replace("[u]", "<u>").replace("[/u]", "</u>");
        text = text.replace("[email]", "<font color=\"#FF9A03\"> ").replace("[/email]", "</font>");
        return text;
    }

    /**
     * 字符 转义字符
     * “ &quot;
     * & &amp;
     * < &lt;
     * > &gt;
     * &nbsp;
     */
    public static String getSource(String escapedHtml) {
        if (escapedHtml == null) return "";
        return escapedHtml.replaceAll("&quot;", "\"").replaceAll("&amp;", "&").replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">").replaceAll("&nbsp;", " ");
    }


/*
    public static TextView createBubbleTextView(Context context, String text){
        //creating textview dynamically
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setBackgroundResource(R.drawable.oval);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.forward, 0, 0, 0);
        return tv;
    }
*/

    public static Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(view.getResources(), viewBmp);
    }

    static class CommentImageGetter implements Html.ImageGetter {
        private TextView textView;

        public CommentImageGetter(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            try {
                Bitmap bm = AcWenApplication.getBitmapInCache(source);
                if (bm == null) {
                    bm = BitmapFactory.decodeStream(textView.getContext().getAssets()
                            .open(source));
                    AcWenApplication.putBitmapInCache(source, bm);
                }
                Drawable drawable = new BitmapDrawable(textView.getResources(), bm);
                if (drawable != null) {
                    int w = textView.getResources().getDimensionPixelSize(
                            R.dimen.emotions_column_width);
                    //指定drawable的绘制时边缘
                    drawable.setBounds(0, 0, w, drawable.getIntrinsicHeight() * w
                            / drawable.getIntrinsicWidth());
                }
                return drawable;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    static class CommentTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            int len = output.length();
            if (opening) {
                if (tag.equalsIgnoreCase("strike")) {
                    output.setSpan(new StrikethroughSpan(), len, len,
                            Spannable.SPAN_MARK_MARK);
                }
            } else {
                if (tag.equalsIgnoreCase("strike")) {
                    end((SpannableStringBuilder) output, StrikethroughSpan.class,
                            new StrikethroughSpan());
                }
            }
        }
    }

}
