/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.bumptech.glide.request.target.ViewTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import mika.com.android.ac.network.NetState;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.util.BitmapCache;

public class AcWenApplication extends Application {

    private static AcWenApplication sInstance;

    private BitmapCache mBitmapCache;
    private Acer acer;
    public int CONNECTIVITY_TYPE;

    public static AcWenApplication getInstance() {
        return sInstance;
    }

    public AcWenApplication() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        ViewTarget.setTagId(R.id.glide_view_target_tag_id);
    }

    private void init() {
        CONNECTIVITY_TYPE = NetState.MOBILE;
        mBitmapCache = new BitmapCache();
    }

    public boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存目录
     *
     * @param dir 缓存文件路径
     * @return 缓存文件
     */
    public File getExternalCacheFiledir(String dir) {
        File cacheFile = new File(sInstance.getExternalCacheDir(), dir);
        //noinspection ResultOfMethodCallIgnored
        cacheFile.mkdirs();
        return cacheFile;
    }

    /**
     * get bitmap cache
     *
     * @param url key
     * @return bitmap
     */
    public Bitmap getBitmapInCache(String url) {
        String key = getCacheKey(url, 0, 0);
        return mBitmapCache.getBitmap(key);
    }

    /**
     * put bitmap cache
     *
     * @param url   key
     * @param value bitmap
     */
    public void putBitmapInCache(String url, Bitmap value) {
        String key = getCacheKey(url, 0, 0);
        mBitmapCache.putBitmap(key, value);
    }

    /**
     * copy from Volley - Image loader
     */
    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return "#W" + maxWidth + "#H" + maxHeight + url;
    }

    public Acer getAcer() {
        return acer;
    }

    public void setAcer(Acer acer) {
        this.acer = acer;
    }

    /**
     * 检测wifi是否连接
     */
    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 夜间模式
     *
     * @return false
     */
    public boolean isNightMode() {
//        return AcApp.getConfig().getBoolean("is_night_mode", false);
        return false;
    }

    private Document sDoc, sDocNight;

    public Document getThemedDoc() throws IOException {
        if (sDoc == null) {
            InputStream in = getInstance().getAssets().open("article.html");
            sDoc = Jsoup.parse(in, "utf-8", "");
        }
        return sDoc;

//        if (isNightMode()) {
//            if (sDocNight == null) {
//                InputStream in = getInstance().getAssets().open("article_night.html");
//                sDocNight = Jsoup.parse(in, "utf-8", "");
//            }
//            return sDocNight;
//        } else {
//            if (sDoc == null) {
//                InputStream in = getInstance().getAssets().open("article.html");
//                sDoc = Jsoup.parse(in, "utf-8", "");
//            }
//            return sDoc;
//        }
    }

}
