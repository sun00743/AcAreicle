/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import mika.com.android.ac.link.UriHandler;

public class UriSpan extends ClickableSpan {

    private String mUri;

    public UriSpan(String uri) {
        mUri = uri;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        UriHandler.open(mUri, widget.getContext());
    }
}
