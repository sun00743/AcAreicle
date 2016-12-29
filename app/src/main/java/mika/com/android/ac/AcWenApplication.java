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
import android.telephony.TelephonyManager;

import com.bumptech.glide.request.target.ViewTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import mika.com.android.ac.network.NetState;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.util.BitmapCache;

public class AcWenApplication extends Application {

    private static AcWenApplication sInstance;

    public AcWenApplication() {
        sInstance = this;
    }

    private static String externalCacheDir;

    public static final String IMAGE = "Images";
    /**
     * 设备udid
     */
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
    private boolean firstLoad = true;

    private Acer acer;

    public int CONNECTIVITY_TYPE;

    public static AcWenApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
//        AndroidThreeTen.init(this);
//        FabricUtils.init(this);
        ViewTarget.setTagId(R.id.glide_view_target_tag_id);
//        getUDID();
    }

    private void getUDID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uuid2 = UUID.randomUUID().toString();
        UDID = deviceUuid.toString();

//        ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void init() {
        CONNECTIVITY_TYPE = NetState.MOBILE;
        density = sInstance.getResources().getDisplayMetrics().density;
        mBitmapCache = new BitmapCache();
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存目录
     *
     * @param dir 缓存文件路径
     * @return 缓存文件
     */
    public static File getExternalCacheFiledir(String dir) {
        File cacheFile = new File(sInstance.getExternalCacheDir(), dir);
        cacheFile.mkdirs();
        return cacheFile;
    }

    /**
     * get bitmap cache
     *
     * @param url key
     * @return bitmap
     */
    public static Bitmap getBitmapInCache(String url) {
        String key = getCacheKey(url, 0, 0);
        return mBitmapCache.getBitmap(key);
    }

    /**
     * put bitmap cache
     *
     * @param url key
     * @param value bitmap
     */
    public static void putBitmapInCache(String url, Bitmap value) {
        String key = getCacheKey(url, 0, 0);
        mBitmapCache.putBitmap(key, value);
    }

    /**
     * copy from Volley - Image loader
     *
     */
    public static String getCacheKey(String url, int maxWidth, int maxHeight) {
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

    public boolean isFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    /**
     * 夜间模式
     *
     * @return false
     */
    public static boolean isNightMode() {
//        return AcApp.getConfig().getBoolean("is_night_mode", false);
        return false;
    }

    private static Document sDoc, sDocNight;

    public static Document getThemedDoc() throws IOException {
        if (isNightMode()) {
            if (sDocNight == null) {
                InputStream in = getInstance().getAssets().open("article_night.html");
                sDocNight = Jsoup.parse(in, "utf-8", "");
            }
            return sDocNight;
        } else {
            if (sDoc == null) {
                InputStream in = getInstance().getAssets().open("article.html");
                sDoc = Jsoup.parse(in, "utf-8", "");
            }
            return sDoc;
        }
    }

}
