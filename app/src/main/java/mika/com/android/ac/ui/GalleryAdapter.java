/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GalleryAdapter extends PagerAdapter {

    private List<String> mImageList;
    private OnTapListener mOnTapListener;
    private boolean isCache;

    public GalleryAdapter(List<String> imageList, OnTapListener onTapListener, boolean hasCache) {
        mImageList = imageList;
        mOnTapListener = onTapListener;
        isCache = hasCache;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View layout = ViewUtils.inflate(R.layout.gallery_item, container);
        PhotoView imageView = ButterKnife.findById(layout, R.id.image);
        final TextView errorText = ButterKnife.findById(layout, R.id.error);
        final ProgressBar progressBar = ButterKnife.findById(layout, R.id.progress);
        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mOnTapListener != null) {
                    mOnTapListener.onTap();
                }
            }
        });
        if (isCache){
            Bitmap bm = BitmapFactory.decodeFile(mImageList.get(position));
            ViewUtils.fadeOut(progressBar);
            imageView.setImageBitmap(bm);
        }else {
            // load from net
            ImageUtils.loadImage(imageView, mImageList.get(position),
                    new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            (e != null ? e : new NullPointerException()).printStackTrace();
                            int errorRes = e != null && e.getCause() instanceof TimeoutError
                                    ? R.string.gallery_load_timeout : R.string.gallery_load_error;
                            errorText.setText(errorRes);
                            ViewUtils.crossfade(progressBar, errorText);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            ViewUtils.fadeOut(progressBar);
                            return false;
                        }
                    });
        }
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnTapListener {
        void onTap();
    }
}
