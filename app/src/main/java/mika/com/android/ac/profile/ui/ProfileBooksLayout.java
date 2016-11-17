/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.content.Context;
import android.util.AttributeSet;

import mika.com.android.ac.link.UriHandler;
import mika.com.android.ac.network.api.info.frodo.Item;

public class ProfileBooksLayout extends ProfileItemsLayout {

    public ProfileBooksLayout(Context context) {
        super(context);
    }

    public ProfileBooksLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileBooksLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Item.Type getItemType() {
        return Item.Type.BOOK;
    }

    @Override
    protected void onViewPrimaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/collect",
                getUserIdOrUid()), getContext());
    }

    @Override
    protected void onViewSecondaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/do", getUserIdOrUid()),
                getContext());
    }

    @Override
    protected void onViewTertiaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/wish", getUserIdOrUid()),
                getContext());
    }
}
