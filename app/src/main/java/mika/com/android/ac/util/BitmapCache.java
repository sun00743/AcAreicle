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
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/9/20.
 * lruCache, 应该把url md5加密一下
 */

public class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String,Bitmap> mCache;

    private final static int DEF_MAX_SIZE = 10<<20;
    private static int MAX_SIZE;
    static {
        //运行内存的1/16 现在基本2G以上了 不用1/8那么大
        MAX_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
        if(MAX_SIZE > DEF_MAX_SIZE)
            MAX_SIZE = DEF_MAX_SIZE;
    }

    public BitmapCache(){
        this(MAX_SIZE);
    }

    public BitmapCache(int maxsize){
        if(maxsize > MAX_SIZE)
            maxsize = MAX_SIZE;

        mCache = new LruCache<String,Bitmap>(maxsize){
            //指定每个bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url,bitmap);
    }
}
