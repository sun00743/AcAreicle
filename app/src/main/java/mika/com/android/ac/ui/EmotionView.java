
/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.IOException;

import mika.com.android.ac.DouyaApplication;


public class EmotionView extends View {
//    private static final String TAG = "EmotionView";
    private int mId;
    private int mWidth;
    private int mHeight;
    private Drawable mDrawable;
    private int mPadding;

    public EmotionView(Context context) {
        super(context);
        mWidth = mHeight = getResources().getDimensionPixelSize(mika.com.android.ac.R.dimen.emotions_column_width);
        mPadding = (int) (4 * DouyaApplication.density + 0.5f);
    }

    public void setEmotionId(int id) {
        if (mId != id) {
            String name = getEmotionName(id);
            try {
                Bitmap bm = DouyaApplication.getBitmapInCache(name);
                if (bm == null) {
                    Options opts = new Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContext().getAssets().open(name), null, opts);
                    if (opts.outWidth > mWidth || opts.outHeight > mHeight) {
                        int sample = Math.max(opts.outWidth / mWidth, opts.outHeight / mHeight);
                        
                        opts.inSampleSize = sample;
//                        Log.d(TAG, String.format("ow=%d,oh=%d, mw=%d,mh=%d, scale to sample=%d",opts.outWidth,opts.outHeight,mWidth,mHeight,sample));
                    }
                    opts.inJustDecodeBounds = false;

                    bm = BitmapFactory
                            .decodeStream(getContext().getAssets().open(name), null, opts);
                    DouyaApplication.putBitmapInCache(name, bm);
//                    Log.d(TAG, "put emotion in cache : " + name);
                }
                mDrawable = new BitmapDrawable(getResources(), bm);

                mDrawable.setBounds(0, 0, mWidth, mDrawable.getIntrinsicHeight()*mHeight/mDrawable.getIntrinsicWidth());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Drawable getmDrawable(){
        return mDrawable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(mWidth+mPadding, widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    public String getEmotionName(int id) {
        if (id > 54 && id < 95) {
            return String.format("emotion/ais/%02d.gif", id - 54);
        }
        if(id > 94){
            return String.format("emotion/ac2/%02d.gif",id - 94);
        }
        return String.format("emotion/%02d.gif", id);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDrawable == null) {
            return;
        }

        int saveCount = canvas.getSaveCount();
        canvas.save();
        mDrawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }
}
