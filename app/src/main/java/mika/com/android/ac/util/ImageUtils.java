/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import mika.com.android.ac.network.api.info.apiv2.Image;
import mika.com.android.ac.ui.RatioImageView;

public class ImageUtils {

    public static void loadAvatar(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(mika.com.android.ac.R.drawable.avatar_icon_grey600_40dp)
                .dontAnimate()
                .dontTransform()
                .into(view);
    }

    public static void loadNavigationHeaderAvatar(final ImageView view, final String url) {
        Context context = view.getContext();
        int size = context.getResources().getDimensionPixelSize(
                mika.com.android.ac.R.dimen.navigation_header_avatar_size);
        Glide.with(context)
                .load(url)
                .placeholder(mika.com.android.ac.R.drawable.avatar_icon_white_inactive_64dp)
                .override(size, size)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        (e != null ? e : new NullPointerException()).printStackTrace();
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        view.setTag(url);
                        return false;
                    }
                })
                .into(view);
    }

    public static void loadNavigationAccountListAvatar(ImageView view, String url) {
        Context context = view.getContext();
        int size = context.getResources().getDimensionPixelSize(
                mika.com.android.ac.R.dimen.navigation_header_avatar_size);
        Glide.with(context)
                .load(url)
                .placeholder(mika.com.android.ac.R.drawable.avatar_icon_grey600_40dp)
                .override(size, size)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .dontTransform()
                .into(view);
    }

    public static void loadProfileAvatarAndFadeIn(final ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .dontAnimate()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        (e != null ? e : new NullPointerException()).printStackTrace();
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        ViewUtils.fadeIn(view);
                        return false;
                    }
                })
                .into(view);
    }

    public static void loadImage(RatioImageView view, Image image) {
        view.setRatio(image.width, image.height);
        Glide.with(view.getContext())
                .load(image.medium)
                // dontTransform() is required for our RatioImageView to work correctly.
                .dontTransform()
                .placeholder(android.R.color.transparent)
                .into(view);
    }

    public static void loadImage(ImageView view, String url,
                                 RequestListener<String, GlideDrawable> listener) {
        Glide.with(view.getContext())
                .load(url)
                .dontTransform()
                .placeholder(android.R.color.transparent)
                .listener(listener)
                .into(view);
    }

    public static void loadImage(ImageView view, String url) {
        loadImage(view, url, null);
    }
}
