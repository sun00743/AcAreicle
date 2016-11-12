/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.bumptech.glide.request.target.ViewTarget;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import me.zhanghai.android.douya.fabric.FabricUtils;
import me.zhanghai.android.douya.network.api.info.acapi.Acer;
import me.zhanghai.android.douya.util.BitmapCache;

public class DouyaApplication extends Application {

    private static DouyaApplication sInstance;

    public DouyaApplication() {
        sInstance = this;
    }

    private static String externalCacheDir;

    public static final String IMAGE = "Images";

    public static String UDID = "";
//    public static String ANDROID_ID = "";
    public static final int ITEM_HEAD = 0x124;
    public static final int ITEM_ARTICLE = 0x123;
    public static final int ITEM_SUBTITLE = 0x125;
    public static float density;
    public static boolean LOGIN;
    public static final int REQUEST_CODE_SIGN_IN = 7;
    public static final int REQUEST_CODE_SETTING = 9;
    private static BitmapCache mBitmapCache;

    private Acer acer;

    public static DouyaApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        AndroidThreeTen.init(this);

        FabricUtils.init(this);

        ViewTarget.setTagId(R.id.glide_view_target_tag_id);

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        UDID = deviceUuid.toString();

//        ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void init() {
        density = sInstance.getResources().getDisplayMetrics().density;
        mBitmapCache = new BitmapCache();
    }

    public static boolean isExternalStorageAvailable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存目录
     * @param dir
     * @return
     */
    public static File getExternalCacheFiledir(String dir){
        File cacheFile = new File(sInstance.getExternalCacheDir(),dir);
        cacheFile.mkdirs();
        return cacheFile;
    }

    /**
     * get bitmap cache
     * @param url
     * @return
     */
    public static Bitmap getBitmapInCache(String url){
        String key = getCacheKey(url, 0, 0);
        return mBitmapCache.getBitmap(key);
    }

    /**
     * put bitmap cache
     * @param url
     * @param value
     */
    public static void putBitmapInCache(String url, Bitmap value){
        String key = getCacheKey(url, 0, 0);
        mBitmapCache.putBitmap(key, value);
    }
    /**
     * copy from Volley - Image loader
     * @param url
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }

    public Acer getAcer() {
        return acer;
    }

    public void setAcer(Acer acer) {
        this.acer = acer;
    }

    /**
     * 夜间模式
     * @return
     */
    public static boolean isNightMode(){
//        return AcApp.getConfig().getBoolean("is_night_mode", false);
        return false;
    }
    private static Document sDoc,sDocNight;
    public static Document getThemedDoc() throws IOException {
        if(isNightMode()){
            if(sDocNight == null){
                InputStream in = getInstance().getAssets().open("article_night.html");
                sDocNight = Jsoup.parse(in, "utf-8", "");
            }
            return sDocNight;
        }else{
            if(sDoc == null){
                InputStream in = getInstance().getAssets().open("article.html");
                sDoc = Jsoup.parse(in, "utf-8", "");
            }
            return sDoc;
        }
    }

}