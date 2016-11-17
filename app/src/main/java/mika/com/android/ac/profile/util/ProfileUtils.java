/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.util;

import android.content.Context;
import android.content.res.Resources;

import mika.com.android.ac.util.CardUtils;
import mika.com.android.ac.util.ViewUtils;

public class ProfileUtils {

    private ProfileUtils() {}

    public static boolean shouldUseWideLayout(Context context) {
        return ViewUtils.hasSw600Dp(context) ? ViewUtils.isInLandscape(context)
                : !CardUtils.isFullWidth(context);
    }

    public static int getAppBarWidth(int width, Context context) {
        if (shouldUseWideLayout(context)) {
            if (CardUtils.getColumnCount(context) == 2) {
                return width * 2 / 5;
            } else {
                Resources resources = context.getResources();
                int cardListHorizontalPadding = resources
                        .getDimensionPixelOffset(mika.com.android.ac.R.dimen.card_list_horizontal_padding);
                int cardHorizontalMargin =
                        resources.getDimensionPixelOffset(mika.com.android.ac.R.dimen.card_horizontal_margin);
                int cardShadowHorizontalMargin =
                        resources.getDimensionPixelOffset(mika.com.android.ac.R.dimen.card_shadow_horizontal_margin);
                return (width - 2 * cardListHorizontalPadding) / 3 + cardListHorizontalPadding
                        + cardHorizontalMargin - cardShadowHorizontalMargin;
            }
        } else {
            return width;
        }
    }
}
