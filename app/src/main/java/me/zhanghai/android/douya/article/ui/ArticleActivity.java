/*
 * Copyright (C) 2013 YROM.NET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhanghai.android.douya.article.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.network.Request;
import me.zhanghai.android.douya.network.Volley;
import me.zhanghai.android.douya.network.api.BaseArticleRequest;
import me.zhanghai.android.douya.network.api.info.acapi.Article;
import me.zhanghai.android.douya.network.api.info.acapi.Constants;
import me.zhanghai.android.douya.util.Connectivity;
import me.zhanghai.android.douya.util.FileUtil;


/**
 * 文章页 结构： {@code <div id="title"> <h1 class="article-title"></h1>
 * <div id="info" class="article-info">
          <span class="article-publisher"><i class="icon-slash"></i></span>
          <span class="article-pubdate"></span>
          <span class="article-category"></span>
        </div>
    </div>
    <section id="content" class="article-body"></section>
    }
 * 
 * @author Yrom
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleActivity extends BaseWebViewActivity implements
        Listener<Article>, ErrorListener {

    private static final Pattern sAreg = Pattern.compile("/a/ac(\\d{5,})");
    private static final Pattern sVreg = Pattern.compile("/v/ac(\\d{5,})");
    private static final Pattern sLiteAreg = Pattern.compile("/v/#ac=(\\d{5,});type=article");
    private static final Pattern sLiteVreg = Pattern.compile("/v/#ac=(\\d{5,})$");
    private static final String sAppReg = "^http://www.acfun.(com|tv)/app/?$";
    public static final int MAX_AGE = 7 * 24 * 60 * 60 * 1000;
    private static String ARTICLE_PATH;
    private static final String NAME_ARTICLE_HTML = "a63-article.html";
    private Request<?> request;
    private Document mDoc;
    private List<String> imgUrls;
    private DownloadImageTask mDownloadTask;
    private String title;
    private boolean isDownloaded;
    private boolean isWebMode;
//    private DB db;

    public static void start(Context context, int aid, String title) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("aid", aid);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("webmode", true);
        context.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void initView(Bundle savedInstanceState) {
        //文章缓存路径
        ARTICLE_PATH = DouyaApplication.getExternalCacheFiledir("article").getAbsolutePath();
        
//        Uri data = getIntent().getData();
//        if(Intent.ACTION_VIEW.equalsIgnoreCase(getIntent().getAction()) && data!=null){
//            String scheme = data.getScheme();
//            if(scheme.equals("ac")){
//                // ac://ac000000
//                aid = Integer.parseInt(getIntent().getDataString().substring(7));
//            }else if(scheme.equals("http")){
//                // http://www.acfun.tv/v/ac123456
//                Matcher matcher;
//                String path = data.getPath();
//                if(path==null){
//                    finish();
//                    return;
//                }
//                if((matcher = sVreg.matcher(path)).find()
//                        || (matcher = sAreg.matcher(path)).find()){
//                    aid = Integer.parseInt(matcher.group(1));
//                }
//            }
//            if(aid != 0) title = "ac"+aid;
//            isWebMode = getIntent().getBooleanExtra("webmode", false) && aid == 0;
//        }else{
            aid = getIntent().getIntExtra("aid", 0);
            title = getIntent().getStringExtra("title");
//        }
        if (!isWebMode) {
            if(aid == 0)
                throw new IllegalArgumentException("没有 id");
            //标题
//            getSupportActionBar().setTitle("ac" + aid);
//            db = new DB(this);
//            isFaved = db.isFav(aid);
        }
        mWeb.getSettings().setAppCachePath(ARTICLE_PATH);
        mWeb.addJavascriptInterface(new ACJSObject(), "AC");
        // Set a chrome client to handle the MediaResource on web page
        // like video,video loading progress, etc.
        mWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }
        });
        mWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Matcher matcher = sAreg.matcher(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                if (matcher.find() || (matcher = sLiteAreg.matcher(url)).find()
//                        || (matcher = sVreg.matcher(url)).find()
//                        || (matcher = sLiteVreg.matcher(url)).find()){
//                    String acId = matcher.group(1);
//                    try {
//                        intent.setData(Uri.parse("ac://ac" + acId));
//                        startActivity(intent);
//                        return true;
//                    } catch (Exception e) {
//                        // nothing
//                    }
//
//                }else if(Pattern.matches(sAppReg, url)){
//                    String appLink = getString(R.string.app_ac_video_link);
//                    try {
//                        intent.setData(Uri.parse(appLink));
//                        startActivity(intent);
//                        return true;
//                    } catch (Exception e) {
//                        view.loadUrl(appLink);
//                        return true;
//                    }
//
//                }
//                if(!isWebMode){
//                    start(ArticleActivity.this, url);
//                    return true;
//                }else{
//                    Uri uri = Uri.parse(url);
//                    if(uri.getHost() != null && !uri.getHost().contains("acfun")){
//                        try {
//                            intent.setData(uri);
//                            startActivity(intent);
//                            return true;
//                        }catch (ActivityNotFoundException ignored){
//                        }
//                    }
//                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                setSupportProgressBarIndeterminateVisibility(true);
            }

            /**
             * 加载完页面后开始异步加载图片
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                setSupportProgressBarIndeterminateVisibility(false);
                if (isWebMode 
                        || imgUrls == null || imgUrls.isEmpty()
                        || url.startsWith("file:///android_asset")
                        || 0 == Constants.MODE_NO_PIC) // 无图模式
                    return;
//                    Log.d(TAG, "on finished:" + url);
                if ((url.equals(getBaseUrl()) || url.contains(NAME_ARTICLE_HTML))
                        && imgUrls.size() > 0 && !isDownloaded) {
                    String[] arr = new String[imgUrls.size()];
                    mDownloadTask = new DownloadImageTask();
                    mDownloadTask.execute(imgUrls.toArray(arr));
                }
                
            }

        });
        mWeb.getSettings().setSupportZoom(true);
        mWeb.getSettings().setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mWeb.getSettings().setDisplayZoomControls(false);
        //设置字号？
        setTextZoom(0
//                AcApp.getConfig().getInt("text_size", 0)
        );
    }

    protected void initData() {
        super.initData();
//        if(isWebMode){
//            Uri uri = getIntent().getData();
//            // TODO: sync cookies to WebView
//            String url = uri.toString();
//            mWeb.loadUrl(url);
//
//            return;
//        }
        request = new ArticleRequest(getApplicationContext(), aid, this, this);
        request.setTag(TAG);
        request.setShouldCache(true);
//        Entry entry = AcApp.getGloableQueue().getCache().get(request.getCacheKey());
//        if (entry != null && entry.data != null && entry.isExpired()) {
//            try {
//                String json = new String(entry.data, "utf-8");
//                JSONObject articleJson = JSON.parseObject(json).getJSONObject("data").getJSONObject("fullArticle");
//                onResponse(Article.newArticle(articleJson));
//                return;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        AcApp.addRequest(request);
        Volley.getInstance().addToRequestQueue(request);

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
    public static Document getThemedDoc(Context context) throws IOException{
        if(isNightMode()){
            if(sDocNight == null){
                InputStream in = context.getAssets().open("article_night.html");
                sDocNight = Jsoup.parse(in, "utf-8", "");
            }
            return sDocNight;
        }else{
            if(sDoc == null){
                InputStream in = context.getAssets().open("article.html");
                sDoc = Jsoup.parse(in, "utf-8", "");
            }
            return sDoc;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWeb.canGoBack()){
            mWeb.goBack();
        }else{
            super.onBackPressed();
        }
    }
    protected String getBaseUrl() {
        return Constants.HOME;
//        return ArticleApi.getDomainRoot(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if(AcApp.getViewMode() != Constants.MODE_COMMIC && !isWebMode){
//            getMenuInflater().inflate(R.menu.article_options_menu, menu);
//            MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
//            if(ActionBarUtil.hasSB()){
//                MenuItemCompat.setShowAsAction(actionItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);
//            }
//            ShareActionProvider actionProvider =  (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
//            actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
//            actionProvider.setShareIntent(createShareIntent());
//            if(isFaved){
//                MenuItem fav = menu.findItem(R.id.menu_item_fav_action);
//                fav.setIcon(R.drawable.rating_favorite_p);
//                fav.setTitle("取消收藏");
//            }
//
//            MenuItem item = menu.add(0, android.R.id.button1, 0, R.string.font_size).setIcon(R.drawable.ic_text_size);
//            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 分享先不管
     * @return
     */
    private Intent createShareIntent() {
//        String shareurl = title + " http://"+ArticleApi.getDomainRoot(getApplicationContext())+"/a/ac" + aid;
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareurl += " ——#Acfun文章区#客户端 http://t.cn/8kLMite";
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, shareurl);
//        return shareIntent;
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//        case R.id.menu_item_comment:
//            if(mArticle != null){
//                CommentsActivity.start(ArticleActivity.this, mArticle.id);
//            }
//            return true;
//        case R.id.menu_item_fav_action:
//            if(isFaved){
//                db.deleteFav(aid);
//                AcApp.showToast("取消收藏");
//                isFaved = false;
//                item.setIcon(R.drawable.rating_favorite);
//            }else{
//                if(mArticle!= null){
//                    db.addFav(mArticle);
//                    isFaved = true;
//                    item.setIcon(R.drawable.rating_favorite_p);
//                    AcApp.showToast("收藏成功");
//                }
//            }
//            return true;
//
//        case android.R.id.button1:
//            if (mSizeChooser == null) {
//                final int checked = AcApp.getConfig().getInt("text_size", 0);
//                mSizeChooser = new AlertDialog.Builder(this)
//                        .setCancelable(true)
//                        .setTitle(R.string.font_size)
//                        .setSingleChoiceItems(R.array.title_sizes, checked,
//                                new DialogInterface.OnClickListener() {
//                                    int lastSelected = checked;
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (lastSelected != which) {
//                                            AcApp.putInt("text_size", which);
//                                            setTextZoom(which);
//                                            dialog.dismiss();
//                                            lastSelected = which;
//                                        }
//                                    }
//                                }).create();
//            }
//            mSizeChooser.show();
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setTextZoom(int level){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            if(level > 4) level = 3;
            mWeb.getSettings().setTextSize(TextSize.values()[level+1]);
        }else
            mWeb.getSettings().setTextZoom(100 + level * 25);
    }

    private String buildTitle(Article article) {
        StringBuilder builder = new StringBuilder();
//        builder
//                .append("<h1 class=\"article-title\">")
//                .append(article.title)
//                .append("</h1>")
//                .append("<div id=\"info\" class=\"article-info\">")
//                .append("<img src=\"")
//                .append(TextUtils.isEmpty(article.poster.avatar) ? "file:///android_asset/wen.png" : article.poster.avatar)
//                .append("\" >")
//                .append("<span class=\"article-publisher\">")
//                .append("<a href=\"http://").append(Constants.HOME).append("/member/user.aspx?uid=")
//                .append(article.poster.id).append("\" >")
//                .append(article.poster.name)
//                .append("</a>").append("</span>")
//                .append("<span class=\"article-pubdate\">")
//                .append(AcApp.getPubDate(article.postTime))
//                .append("发布于</span>")
//                .append("<span class=\"article-category\">")
//                .append(article.channelName)
//                .append("</span>")
//                .append("</div>");

        return builder.toString();
    }

    private static final String TAG = "Article";
    private Article mArticle;
    private AlertDialog mSizeChooser;
    static class ArticleRequest extends BaseArticleRequest<Article> {
        private static final String ARTICLE_API =  "http://api.aixifan.com/articles/";

        public ArticleRequest(Context context, int aid, Listener<Article> listener, ErrorListener errListener) {
            super(ARTICLE_API + aid, Article.class, listener, errListener);
        }

        @Override
        protected Response<Article> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                JSONObject rsp = JSON.parseObject(json);
                int code = rsp.getIntValue("code");
                if(code != 200){
                    throw new Article.InvalidArticleError();
                }
                JSONObject articleJson = rsp.getJSONObject("data");
                if(articleJson == null)
                    return Response.error(new Article.InvalidArticleError());
                return Response.success(Article.newArticle(articleJson),
                        Connectivity.newCache(response, MAX_AGE));
            } catch(Article.InvalidArticleError e){
                Log.w(TAG, "Invalid Article! Need to redirect intent");
                return Response.error(e);
            } catch (Exception e) {
                Log.e(TAG, "parse article error", e);
                return Response.error(new ParseError(e));
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isWebMode // no images
                || isDocBuilding.get() || imageCaches == null // building doc
                // on request data 
                || mArticle == null || imgUrls == null || imgUrls.isEmpty()
                || 0 == Constants.MODE_NO_PIC)
            return;
        if (!isDownloaded && imgUrls.size() > 0) {
            String[] arr = new String[imgUrls.size()];
            mDownloadTask = new DownloadImageTask();
            mDownloadTask.execute(imgUrls.toArray(arr));
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mDownloadTask != null && !isDownloaded) {
            mDownloadTask.cancel(false);
            mDownloadTask = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AcApp.cancelAllRequest(TAG);
        Volley.getInstance().cancelRequests(TAG);
    }

    @Override
    public void onResponse(Article response) {
        mArticle = response;
        imgUrls = response.imgUrls;
//        KeepOnlineService.requestOnline(getApplicationContext(), aid);
//        if(0 == Constants.MODE_COMMIC && imgUrls != null && !imgUrls.isEmpty()){
//            ImagePagerActivity.startNetworkImage(this, (ArrayList<String>) imgUrls,0,aid,title);
//            finish();
//        }
//        else
            new BuildDocTask().execute(mArticle);

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        setSupportProgressBarIndeterminateVisibility(false);
        if(error instanceof Article.InvalidArticleError){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("av://ac"+aid));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
                finish();
            } catch (Exception e) {
                //http://www.acfun.tv/lite/v/#ac=1317054
                mWeb.loadUrl("http://"+Constants.HOME+"/lite/v/#ac="+aid);
            }
        }else if(error instanceof ServerError){
            mWeb.loadUrl("http://"+Constants.HOME+"/lite/v/#ac="+aid);
        }else{
            showErrorDialog();
        }
    }

    List<File> imageCaches;
    private int aid;
    private boolean isFaved;
    private AtomicBoolean isDocBuilding = new AtomicBoolean(false);
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class BuildDocTask extends AsyncTask<Article, Void, Boolean> {
        boolean hasUseMap;
        private File cacheFile;

        @Override
        protected void onPreExecute() {
            isDocBuilding.set(true);
            cacheFile =new File(ARTICLE_PATH, NAME_ARTICLE_HTML);
        }

        @Override
        protected Boolean doInBackground(Article... params) {
            try {
                mDoc = getThemedDoc(ArticleActivity.this);
                initCaches();

                Element content = mDoc.getElementById("content");
                content.empty();

                ArrayList<Article.SubContent> contents = params[0].contents;
                if(contents.size()>1){
                    //构建内容html
                    content.appendElement("div").attr("id", "artcle-pager")
                            .html(buildParts(contents));
                }
                for (int i = 0; i < contents.size(); i++) {
                    Article.SubContent sub = contents.get(i);
                    handleSubContent(i, content, sub, params[0]);
                }
                FileWriter writer = null;
                try{
                    writer = new FileWriter(cacheFile);
                    writer.write(mDoc.outerHtml());
                    content.empty(); // release
                }catch(IOException e){
                    cacheFile.delete();
                }finally{
                    IOUtils.close(writer);
                }
                
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        private String buildParts(ArrayList<Article.SubContent> contents) {
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<contents.size();i++){
                builder.append("<li><a class=\"pager\" href=\"#p")
                    .append(i).append("\" title=\"Part ").append(i+1).append("\">")
                    .append(contents.get(i).subTitle)
                    .append("</a></li>");
            }
            builder.append("<hr>");
            return builder.toString();
        }

        private void handleSubContent(int p, Element content, Article.SubContent sub, Article article) {
            if (!article.title.equals(sub.subTitle)) {
                content.append("<h2 class=\"article-subtitle\"><a class=\"anchor\" name=\"p"+p+"\"></a>Part "+(p+1)+". "+sub.subTitle + "</h2>");
            }
            String data = sub.content.replaceAll("background-color:[^;\"]+;?", "").replaceAll("font-family:[^;\"]+;?", "");
            
            content.append(data).appendElement("hr");
            handleImages(content);
            handleStyles(content);
        }
        private void handleStyles(Element content) {
            Elements es = content.getAllElements();
            
            for (int i = 0; i < es.size(); i++) {
                Element e = es.get(i);
//                if("span".equals(e.nodeName())){
//                    continue;
//                }
                e.removeAttr("style");
                
            }
        }

        private void handleImages(Element content){
            Elements imgs = content.select("img");
            if (imgs.hasAttr("usemap")) {
                hasUseMap = true;
            }
            for (int imgIndex = 0; imgIndex < imgs.size(); imgIndex++) {
                Element img = imgs.get(imgIndex);
                String src = img.attr("src").trim();
                if (TextUtils.isEmpty(src))
                    continue;
                Uri parsedUri = Uri.parse(src);
                if ("file".equals(parsedUri.getScheme()))
                    continue;
                if (parsedUri.getPath() == null) // wtf!
                    continue;
                if (!"http".equals(parsedUri.getScheme())) {
                    parsedUri = parsedUri.buildUpon()
                            .scheme("http")
                            .authority(Constants.HOME)
                            .build();
                }
                // url may have encoded path
                parsedUri = parsedUri.buildUpon().path(parsedUri.getPath()).build();
                src = parsedUri.toString();
                File cache = FileUtil.generateImageCacheFile(src);
                imageCaches.add(cache);
                imgUrls.add(src);
                img.attr("org", src);
                String localUri = FileUtil.getLocalFileUri(cache).toString();

                if (0 != Constants.MODE_NO_PIC)
                    img.attr("src", "file:///android_asset/loading.gif");
                else {
                    // 无图模式
                    // TODO 点击后加载图片
                    img.after("<p >[图片]</p>");
                    img.remove();
                    continue;
                }
                img.attr("loc", localUri);
                // 去掉 style
                img.removeAttr("style");
                
                
                // 给 img 标签加上点击事件
                if (!hasUseMap){
                    addClick(img, src);
                    img.removeAttr("width");
                    img.removeAttr("height");
                }
            }
        }
        private void initCaches() {
            if (imgUrls != null)
                imgUrls.clear();
            else
                imgUrls = new ArrayList<>();
            if (imageCaches != null)
                imageCaches.clear();
            else
                imageCaches = new ArrayList<>();
        }

        private void addClick(Element img, String src) {
            try {
                if ("icon".equals(img.attr("class")) || Integer.parseInt(img.attr("width")) < 100
                        || Integer.parseInt(img.attr("height")) < 100) {
                    return;
                }
            } catch (Exception ignored) {
            }
            if (src.contains("emotion/images/"))
                return;
            // 过滤掉图片的url跳转
            if (img.parent() != null && img.parent().tagName().equalsIgnoreCase("a")) {
                img.parent().attr("href", "javascript:window.AC.viewImage('" + src + "');");
            } else {
                img.attr("onclick", "javascript:window.AC.viewImage('" + src + "');");
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            isDocBuilding.set(false);
            if(isFinishing()) return;
            setSupportProgressBarIndeterminateVisibility(false);

            if (result) {
                if(cacheFile.exists()){
                    mWeb.loadUrl(Uri.fromFile(cacheFile).toString());
                }else
                mWeb.loadDataWithBaseURL(getBaseUrl(), mDoc.html(), "text/html", "UTF-8", null);
                
                if (hasUseMap)
                    mWeb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
                else
                    mWeb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    try {
                        mWeb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }

    };

    /**
     * 异步下载图片到缓存目录
     */
    private class DownloadImageTask extends AsyncTask<String, Integer, Void> {

        int timeoutMs = 3000;
        int tryTimes = 3;

        @Override
        protected Void doInBackground(String... params) {
            for (int index = 0; index < params.length; index++) {
                String url = params[index];
                if (isCancelled()) {
                    // cancel task on activity destory
//                    Log.w(TAG, String.format("break download task,[%d/%d]", index+1, params.length));
                    break;
                }
                File cache = imageCaches.get(imgUrls.indexOf(url));
                if (cache.exists() && cache.canRead()) {
//                    Log.i(TAG, String.format("already downloaded.[%d/%d]",index+1, params.length));
                    publishProgress(index);
                    continue;
                } else {
                    cache.getParentFile().mkdirs();
                }
                File temp = new File(cache.getAbsolutePath()+".tmp");
                
                InputStream in = null;
                OutputStream out = null;

                try {
                    URL parsedUrl = new URL(url);
                    retry: for (int i = 0; i < tryTimes && !isCancelled(); i++) {
                        
                        HttpURLConnection connection = Connectivity.openDefaultConnection(parsedUrl, timeoutMs*(1+i/2), (timeoutMs * (2 + i)));
                        if(temp.exists()){
                            connection.addRequestProperty("Range", "bytes="+temp.length()+"-");
                            out = new FileOutputStream(temp,true);
                        }else
                            out = new FileOutputStream(temp);
                        try {
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200 || responseCode == 206) {
                                in = connection.getInputStream();
                                FileUtil.copyStream(in, out);
                                cache.delete();
                                if(!temp.renameTo(cache)){
                                    Log.w(TAG, "重命名失败"+temp.getName());
                                }
                                publishProgress(index);
                                break retry;
                            }
                        } catch (SocketTimeoutException e) {
                            Log.w(TAG, "retry", e);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null)
                            in.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException ignored) {
                    }
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (imgUrls != null) {
                String url = imgUrls.get(values[0]);
                if (url == null)
                    return;
                StringBuilder jsBuilder = new StringBuilder();
                jsBuilder.append("javascript:(function(){")
                         .append("var images = document.getElementsByTagName(\"img\"); ")
                         .append("var img = images[").append(values[0]+1).append("];")
                         .append("img.src = img.getAttribute(\"loc\");")
                         .append("})()");
                evaluateJavascript(jsBuilder.toString(),null);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            // 确保所有图片都顺利的显示出来
            isDownloaded = true;
            evaluateJavascript("javascript:(function(){"
                    + "var images = document.getElementsByTagName(\"img\"); "
                    + "for(var i=0;i<images.length;i++){"
                    +   "var imgSrc = images[i].getAttribute(\"loc\"); "
                    +   "if(imgSrc != null)"
                    +   "images[i].setAttribute(\"src\",imgSrc);"
                    + "}"
                    + "})()",
                    null);
        }

    }
    
    
    class ACJSObject {
        @android.webkit.JavascriptInterface
        public void viewcomment() {
//            CommentsActivity.start(ArticleActivity.this, mArticle.id);
        }

        @android.webkit.JavascriptInterface
        public void viewImage(String url) {
//            ImagePagerActivity.startCacheImage(ArticleActivity.this,
//                    (ArrayList<File>) imageCaches,
//                    imgUrls.indexOf(url), aid, title);
        }
    }

}
