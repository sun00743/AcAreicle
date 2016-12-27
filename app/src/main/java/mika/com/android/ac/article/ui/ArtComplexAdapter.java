/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.article.ui;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.util.IOUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.network.NetState;
import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.ArticleRequest;
import mika.com.android.ac.network.api.info.acapi.Article;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.network.api.info.acapi.Constants;
import mika.com.android.ac.ui.FloorsView;
import mika.com.android.ac.ui.SimpleAdapter;
import mika.com.android.ac.ui.SimpleCircleImageView;
import mika.com.android.ac.ui.TimeActionTextView;
import mika.com.android.ac.util.Connectivity;
import mika.com.android.ac.util.DateUtils;
import mika.com.android.ac.util.DensityUtil;
import mika.com.android.ac.util.FileUtil;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.TextViewUtils;
import mika.com.android.ac.util.ViewUtils;

/**
 * Complex
 */
public class ArtComplexAdapter extends SimpleAdapter<Integer, RecyclerView.ViewHolder> {

    private static final String UA = "acfun/1.0 (Linux; U; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + "; " + Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry().toLowerCase() + ") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";
    private static final int VIEW_TYPE_ARTICLE = 0x02;
    private static final int VIEW_TYPE_COMMENT = 0x04;
    private static final int VIEW_TYPE_SUBTITLE = 0x03;
    private static final int VIEW_TYPE_HEAD = 0x01;

    private ArticleActivity2 activity;

    private static final String TAG = "Article";
    private static String ARTICLE_PATH;
    private static final String NAME_ARTICLE_HTML = "a63-article.html";
    private Article mArticle;
    private Document mDoc;
    private List<String> imgUrls;
    private DownloadImageTask mDownloadTask;
    //    private String title;
    private boolean isDownloaded;
//    private boolean isWebMode;

    private int aid;
    private Bundle mBundle;
    private List<Integer> mCommentIdList;
    private SparseArray<Comment> mCommentList;

    private EventListener mEventListener;
    private ArticleHolder mCurrentHolder;
    private SubTitleHolder mSubTitleHolder;
    private HeadHolder mHeadHolder;

    //    private int mWebViewHeight;
    private boolean isContentFirstLoad = true;
    private boolean isHeadFirstLoad = true;
    private boolean hasBundle = true;

    public void setAutoLoad(boolean autoLoad) {
        isAutoLoad = autoLoad;
    }

    private boolean isAutoLoad = false;

    public ArtComplexAdapter(List<Integer> list, ArticleActivity2 activity, Bundle bundle) {
        super(null);
        mCommentIdList = list;
        this.activity = activity;
        mBundle = bundle;
        this.aid = bundle.getInt("aid");
//        this.title = bundle.getString("title");
        mCommentList = new SparseArray<>();
    }

    @Override
    public long getItemId(int position) {
        return mCommentIdList.get(position);
    }

    @Override
    public Integer getItem(int position) {
        return mCommentIdList.get(position);
    }

    @Override
    public int getItemCount() {
        return mCommentIdList.size();
    }

    private Comment getComment(int position) {
        return mCommentList.get(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE;
        switch (position) {
            case 0:
                VIEW_TYPE = 0x01;
                break;
            case 1:
                VIEW_TYPE = 0x02;
                break;
            case 2:
                VIEW_TYPE = 0x03;
                break;
            default:
                VIEW_TYPE = 0x04;
                break;
        }
        return VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEAD:
                return new HeadHolder(ViewUtils.inflate(R.layout.articlecomplex_item_head, parent));
            case VIEW_TYPE_ARTICLE:
                return new ArticleHolder(ViewUtils.inflate(R.layout.articlecomplex_item_article, parent));
            case VIEW_TYPE_SUBTITLE:
                return new SubTitleHolder(ViewUtils.inflate(R.layout.articlecomplex_item_subtitle, parent));
            default:
                return new CommentHolder(ViewUtils.inflate(R.layout.articlecomplex_item_comment, parent));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEAD:
                if (isHeadFirstLoad) {
                    isHeadFirstLoad = false;
//                    mEventListener.setHeadView(mHeadHolder.headContent);
                    if (mBundle != null && mBundle.getString("username") != null) {
                        ImageUtils.loadAvatar(((HeadHolder) holder).avatar, mBundle.getString("avatar"));
                        ((HeadHolder) holder).username.setText(mBundle.getString("username"));
                        ((HeadHolder) holder).time.setText(mBundle.getString("time"));
                        ((HeadHolder) holder).viewCount.setText(mBundle.getLong("view_count") + " " + "围观");
                        ((HeadHolder) holder).articleTitle.setText(mBundle.getString("title"));
                    } else {
                        mHeadHolder = (HeadHolder) holder;
                        hasBundle = false;
                    }
                }
                break;
            case VIEW_TYPE_ARTICLE:
                mCurrentHolder = (ArticleHolder) holder;
                if (isContentFirstLoad) {
                    isContentFirstLoad = false;
//                    ((ArticleHolder) holder).setSupportProgressBarIndeterminateVisibility(true);
                    ((ArticleHolder) holder).mWeb.getSettings().setAllowFileAccess(true);
                    ((ArticleHolder) holder).mWeb.getSettings().setJavaScriptEnabled(true);
                    ((ArticleHolder) holder).mWeb.getSettings().setUserAgentString(UA);
                    ((ArticleHolder) holder).mWeb.getSettings().setUseWideViewPort(true);
                    ((ArticleHolder) holder).mWeb.getSettings().setLoadWithOverviewMode(true);
                    initView();
                    requestData();
                }
                break;
            case VIEW_TYPE_SUBTITLE:
                mSubTitleHolder = (SubTitleHolder) holder;
                mSubTitleHolder.updateComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSubTitleHolder.subtitle_pro.setVisibility(View.VISIBLE);
                        isAutoLoad = false;
                        mEventListener.updateComment();
                    }
                });
                break;
            default:
                final Comment comment = getComment(position);
                CommentHolder cHolder = (CommentHolder) holder;
                releaseComment((CommentHolder) holder);

                if (mArticle.poster.id == comment.userId) {
                    cHolder.userName.setText("#" + comment.floor + " #up " + comment.username);
                } else {
                    cHolder.userName.setText("#" + comment.floor + " " + comment.username);
                }
                cHolder.tiem.setText(comment.calculateTimeDiff());
                TextViewUtils.setCommentContent(cHolder.content, comment);
                ImageUtils.loadAvatar(cHolder.avatar, comment.avatar);
                cHolder.quoted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEventListener.insertComment(comment);
                    }
                });
//                cHolder.more.setOnClickListener();

                int quoteId = comment.quoteId;
                cHolder.hasQuote = quoteId > 0;
                List<View> quoteViewList = new ArrayList<>();
                //添加引用的views到list中
                addQuoteViews(position, cHolder, quoteId, quoteViewList);     //添加引用评论view
                cHolder.quoteItemsView.setQuoteList(quoteViewList);
                if (!quoteViewList.isEmpty()) {
                    RelativeLayout.LayoutParams quoteItemsParams = new RelativeLayout.LayoutParams(-1, -2);
                    quoteItemsParams.addRule(RelativeLayout.BELOW, R.id.comment_requote);
                    cHolder.reQuoteContent.addView(cHolder.quoteItemsView, quoteItemsParams);
                }
                setItemPadding(cHolder);
                break;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == VIEW_TYPE_COMMENT) {
            releaseComment((CommentHolder) holder);
        }
        super.onViewRecycled(holder);
    }

    private void releaseComment(CommentHolder holder) {
        holder.reQuoteContent.removeView(holder.quoteItemsView);
        holder.hasQuote = false;
        holder.reQuote.setVisibility(View.GONE);
    }

    /**
     * 初次加载/刷新
     *
     * @param commentIdList 评论id list
     * @param commentMaps   评论bean map
     */
    public void replace(Collection<? extends Integer> commentIdList,
                        SparseArray<Comment> commentMaps) {
        int oldSize = mCommentIdList.size() - 3;
        int newSize = commentIdList.size();
        int diff = newSize - oldSize;
        // if newSize == 0, hasn't new comment
        if (newSize == 0) return;
        if (diff > 0 || !mCommentIdList.get(3).equals(((ArrayList) commentIdList).get(0))) {
            mCommentIdList.clear();
            mCommentIdList.add(AcWenApplication.ITEM_HEAD);
            mCommentIdList.add(AcWenApplication.ITEM_ARTICLE);
            mCommentIdList.add(AcWenApplication.ITEM_SUBTITLE);
            mCommentIdList.addAll(commentIdList);
            mCommentList = commentMaps;

            notifyItemRangeInserted(3, newSize);
            if (!isAutoLoad) {
                mEventListener.dataReplaceOk();
                notifyItemRangeRemoved(3 + newSize, oldSize);
            }
        }
    }

    /**
     * 加载更多
     * Using
     *
     * @param commentIdList 评论id list
     * @param commentMaps   评论bean map
     */
    public void insert(List<? extends Integer> commentIdList,
                       Map<String, Comment> commentMaps) {
        int oldSize = mCommentIdList.size();
        mCommentIdList.addAll(commentIdList);
        for (Comment c : commentMaps.values()) {
            mCommentList.append(c.id, c);
        }
        notifyItemRangeInserted(oldSize, commentIdList.size());
    }

    /**
     * 加载更多
     */
    public void insert(List<Comment> commentList) {
        int oldSize = mCommentIdList.size();
        for (int i = 0; i < commentList.size(); i++) {
            mCommentIdList.add(commentList.get(i).id);
            mCommentList.append(commentList.get(i).id, commentList.get(i));
        }
        notifyItemRangeInserted(oldSize, commentList.size());
    }

    /**
     * 向引用的评论列表里添加 quoteView
     */
    private void addQuoteViews(int position, CommentHolder cHolder, int quoteId, List<View> quoteViewList) {
        if (cHolder.hasQuote || cHolder.quoteItemsView == null) {
            FloorsView floors = new FloorsView(activity);
            floors.setId(R.id.quote_item_floor);
            cHolder.quoteItemsView = floors;
        }

        //引用评论数number , 50层封顶
        int n = 0;
        for (Comment quote = mCommentList.get(quoteId); quote != null && n < 50;
             quoteId = quote.quoteId, quote = mCommentList.get(quoteId), n++) {

            //是否被引用过了
            if (quote.isQuoted) {
                // position < floor, position在上

                // 引用floor比position小, 说明引用floor在当前floor之上
//                if(quote.quotedFloor < position){
//                    //显示已被引用
//                    cHolder.reQuote.setVisibility(View.VISIBLE);
//                }else {
//                    quote.quotedFloor = position;
//                    quoteViewList.add(generateQuotesView(quote));
//                }

                //是否在同一层楼
                if (quote.quotedFloor == position) {
                    quoteViewList.add(generateQuotesView(quote));
                    //引用的楼层比当前的楼城高
                } else if (quote.quotedFloor > position) {
                    quote.quotedFloor = position;
                    quoteViewList.add(generateQuotesView(quote));
                } else {
                    //显示引用提示
                    cHolder.reQuote.setVisibility(View.VISIBLE);
                }
            } else {
                quote.isQuoted = true;
                //用quotedFloor来记住被引用的楼层
                quote.quotedFloor = position;
                quoteViewList.add(generateQuotesView(quote));
            }

        }
    }

    /**
     * 生成引用的评论的view
     */
    private View generateQuotesView(final Comment quote) {
        RelativeLayout quoteLayout = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.articlecomplex_item_quote, null);
        TextView username = (TextView) quoteLayout.findViewById(R.id.quote_item_username);
        TextView content = (TextView) quoteLayout.findViewById(R.id.quote_item_comments_content);
        SimpleCircleImageView avatar = (SimpleCircleImageView) quoteLayout.findViewById(R.id.quote_item_avatar);
        ImageView quoted = (ImageView) quoteLayout.findViewById(R.id.comment_quoteimg);
        ImageView more = (ImageView) quoteLayout.findViewById(R.id.comment_more);

        if (mArticle.poster.id == quote.userId) {
            username.setText("#" + quote.floor + " #up " + quote.username);
        } else {
            username.setText("#" + quote.floor + " " + quote.username);
        }
        TextViewUtils.setCommentContent(content, quote);
        quoted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.insertComment(quote);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/12/5   评论右上更多按钮
            }
        });
        ImageUtils.loadAvatar(avatar, quote.avatar);
        return quoteLayout;
    }


    private void setItemPadding(CommentHolder cHolder) {
        int padding = DensityUtil.dip2px(activity, 8);
        cHolder.itemView.setPadding(0, padding, 0, 0);
    }

    private void initView() {
        //文章缓存路径
        ARTICLE_PATH = AcWenApplication.getExternalCacheFiledir("article").getAbsolutePath();
        if (aid == 0)
            throw new IllegalArgumentException("没有 id");
        mCurrentHolder.mWeb.getSettings().setAppCachePath(ARTICLE_PATH);
        mCurrentHolder.mWeb.addJavascriptInterface(new ACJSObject(), "AC");
        mCurrentHolder.mWeb.setWebViewClient(new WebViewClient() {

            /**
             * 加载完页面后开始异步加载图片
             */
            @Override
            public void onPageFinished(final WebView view, String url) {

                mEventListener.ProgressDismiss();
                //如果不出错
                mSubTitleHolder.subtitle.setVisibility(View.VISIBLE);

                if (AcWenApplication.getInstance().CONNECTIVITY_TYPE == NetState.WIFI) {
                    //同时加载评论
                    isAutoLoad = true;
                    activity.loadComment();
                }

                if (imgUrls == null || imgUrls.isEmpty() || url.startsWith("file:///android_asset")
                        || 0 == Constants.MODE_NO_PIC) // 无图模式
                    return;

                if ((url.equals(Constants.HOME) || url.contains(NAME_ARTICLE_HTML))
                        && imgUrls.size() > 0
                        && !isDownloaded
                        && AcWenApplication.getInstance().CONNECTIVITY_TYPE == NetState.WIFI) {
                    String[] arr = new String[imgUrls.size()];
                    mDownloadTask = new DownloadImageTask();
                    mDownloadTask.execute(imgUrls.toArray(arr));
                }

            }

        });
        mCurrentHolder.mWeb.getSettings().setSupportZoom(true);
        mCurrentHolder.mWeb.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mCurrentHolder.mWeb.getSettings().setDisplayZoomControls(false);
        //设置字号？
//        setTextZoom(0);
    }

    /**
     * load article data
     */
    private void requestData() {
        Request<?> request = new ArticleRequest(aid, new Response.Listener<Article>() {
            @Override
            public void onResponse(Article response) {
                mArticle = response;
                imgUrls = response.imgUrls;
                new BuildDocTask().execute(mArticle);
                // bundle中无 关键 数据，说明是从quote点击进入，需要填充head数据
                if (!hasBundle) {
                    ImageUtils.loadAvatar(mHeadHolder.avatar, mArticle.poster.avatar);
                    mHeadHolder.username.setText(mArticle.poster.name);
                    mHeadHolder.time.setText(DateUtils.formatAgoTimes(System.currentTimeMillis() - mArticle.postTime));
                    mHeadHolder.viewCount.setText(mArticle.views + " " + activity.getResources().getString(R.string.article_views));
                    mHeadHolder.articleTitle.setText(mArticle.title);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                mEventListener.ProgressDismiss();

            }
        });
        request.setTag(TAG);
        request.setShouldCache(true);
        Volley.getInstance().addToRequestQueue(request);
    }

    /**
     * subtitile刷新评论 progress Gone
     */
    public void setUpdataProgressGONE() {
        mSubTitleHolder.subtitle_pro.setVisibility(View.GONE);
    }

    public void pause() {
        mCurrentHolder.mWeb.pauseTimers();
    }

    public void resume() {
        if (mCurrentHolder != null) {
            mCurrentHolder.mWeb.resumeTimers();
        }
    }

    public View getHeadView() {
        return mHeadHolder.headContent;
    }

    public View getArticleView() {
        return mCurrentHolder.mContent;
    }

    public void setEventListener(EventListener listener) {
        mEventListener = listener;
    }

    interface EventListener {

        void ProgressDismiss();

        /**
         * 引用评论
         */
        void insertComment(Comment quote);

        /**
         * 刷新评论
         */
        void updateComment();

        void dataReplaceOk();

        /**
         * @param headview headview
         */
        void setHeadView(View headview);
    }

    /**
     * 标题头Viewholder
     */
    static class HeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_head_content)
        RelativeLayout headContent;
        @BindView(R.id.user_avatar)
        SimpleCircleImageView avatar;
        @BindView(R.id.user_name)
        TextView username;
        @BindView(R.id.time_format)
        TimeActionTextView time;
        @BindView(R.id.view_count)
        TimeActionTextView viewCount;
        @BindView(R.id.article_title)
        TextView articleTitle;

        HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 文章viewholder
     */
    static class ArticleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_content)
        RelativeLayout mContent;
        @BindView(R.id.article_webview)
        WebView mWeb;

        ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setSupportProgressBarIndeterminateVisibility(boolean visible) {
            mWeb.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        }

        /**
         * @param script         the JavaScript to execute.
         * @param resultCallback A callback to be invoked when the script execution completes
         *                       with the result of the execution (if any). May be null if no
         *                       notificaion of the result is required.
         */
        void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    mWeb.evaluateJavascript(script, resultCallback);
                    return;
                } catch (Exception ignored) {
                }
            }
            mWeb.loadUrl(script);
        }
    }

    /**
     * 评论Viewholder
     */
    static class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_requote_content)
        RelativeLayout reQuoteContent;

        FloorsView quoteItemsView;
        boolean hasQuote;

        @BindView(R.id.comments_content)
        TextView content;

        @BindView(R.id.comment_username)
        TextView userName;
        /**
         * 引用当前条目
         */
        @BindView(R.id.comment_quoteimg)
        ImageView quoted;
        /**
         * 更多选择
         */
        @BindView(R.id.comment_more)
        ImageView more;

        @BindView(R.id.comment_avatar)
        SimpleCircleImageView avatar;
        @BindView(R.id.comment_time)
        TextView tiem;
        /**
         * 重复引用提示
         */
        @BindView(R.id.comment_requote)
        TextView reQuote;

        CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 副标题Viewholder
     */
    static class SubTitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subtitle_comment)
        RelativeLayout subtitle;
        @BindView(R.id.subtitle_update_comment)
        ImageButton updateComment;
        @BindView(R.id.subtitle_Progress)
        ProgressBar subtitle_pro;

        SubTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<File> imageCaches;
    //是否收藏
    //    private boolean isFaved;
    private AtomicBoolean isDocBuilding = new AtomicBoolean(false);

    /**
     * 读取html文档异步任务
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class BuildDocTask extends AsyncTask<Article, Void, Boolean> {
        boolean hasUseMap;
        private File cacheFile = null;

        @Override
        protected void onPreExecute() {
            isDocBuilding.set(true);
            cacheFile = new File(ARTICLE_PATH, NAME_ARTICLE_HTML);
        }

        @Override
        protected Boolean doInBackground(Article... params) {
            try {
                mDoc = AcWenApplication.getThemedDoc();
                initCaches();

                Element content = mDoc.getElementById("content");
                content.empty();

                ArrayList<Article.SubContent> contents = params[0].contents;
                if (contents.size() > 1) {
                    //构建内容html
                    content.appendElement("div").attr("id", "artcle-pager")
                            .html(buildParts(contents));
                }
                for (int i = 0; i < contents.size(); i++) {
                    Article.SubContent sub = contents.get(i);
                    //build content
                    handleSubContent(i, content, sub, params[0]);
                }
                FileWriter writer = null;
                try {
                    writer = new FileWriter(cacheFile);
                    writer.write(mDoc.outerHtml());
                    content.empty(); // release
                } catch (IOException e) {
                    cacheFile.delete();
                } finally {
                    IOUtils.close(writer);
                }

            } catch (IOException e) {
                return false;
            }
            return true;
        }

        private String buildParts(ArrayList<Article.SubContent> contents) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < contents.size(); i++) {
                builder.append("<li><a class=\"pager\" href=\"#p")
                        .append(i).append("\" title=\"Part ").append(i + 1).append("\">")
                        .append(contents.get(i).subTitle)
                        .append("</a></li>");
            }
            builder.append("<hr>");
            return builder.toString();
        }

        private void handleSubContent(int p, Element content, Article.SubContent sub, Article article) {
            if (!article.title.equals(sub.subTitle)) {
                content.append("<h2 class=\"article-subtitle\"><a class=\"anchor\" name=\"p" + p + "\"></a>Part " + (p + 1) + ". " + sub.subTitle + "</h2>");
            }
            String data = sub.content.replaceAll("background-color:[^;\"]+;?", "").replaceAll("font-family:[^;\"]+;?", "");

            content.append(data);
//                    .appendElement("hr");
            handleImages(content);
            handleStyles(content);
        }

        private void handleStyles(Element content) {
            Elements es = content.getAllElements();

            for (int i = 0; i < es.size(); i++) {
                Element e = es.get(i);

                ///这个地方
//                if("span".equals(e.nodeName())){
//                    continue;
//                }
                e.removeAttr("style");
            }
        }

        private void handleImages(Element content) {
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
                            .authority("www.acfun.tv")
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

                if (0 == Constants.MODE_NO_PIC || AcWenApplication.getInstance().CONNECTIVITY_TYPE == NetState.WIFI)
                    img.attr("src", "file:///android_asset/loading.gif");
                else {
//                     no image mode , click to load and display image
//                    img.after("<p >[图片]</p>");
//                    img.remove();
                    img.attr("src", "file:///android_asset/emotion/td/08.gif");
                    img.attr("loc", localUri);
                    img.removeAttr("style");
                    showImgClick(img, src, imgIndex);
                    continue;
                }
                img.attr("loc", localUri);
                // 去掉 style
                img.removeAttr("style");
                // 给 img 标签加上点击事件
                if (!hasUseMap) {
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

        private void showImgClick(Element img, String src, int index) {
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
//                img.parent().attr("href", "javascript:window.AC.showImage('" + src + "');");
                img.parent().attr("onclick", "javascript:window.AC.showImage('" + src + " ',' " + index + "');");
            } else {
//                img.attr("onclick", "javascript:window.AC.showImage('" + src + "');");
                img.attr("onclick", "javascript:window.AC.showImage('" + src + " ',' " + index + "');");
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            isDocBuilding.set(false);
            if (activity.isFinishing()) return;
//            mCurrentHolder.setSupportProgressBarIndeterminateVisibility(false);
            if (result) {
                if (cacheFile.exists()) {
                    mCurrentHolder.mWeb.loadUrl(Uri.fromFile(cacheFile).toString());
                } else
                    mCurrentHolder.mWeb.loadDataWithBaseURL("www.acfun.tv", mDoc.html(), "text/html", "UTF-8", null);

                if (hasUseMap)
                    mCurrentHolder.mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                else
                    mCurrentHolder.mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        mCurrentHolder.mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }

        }
    }

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
                File temp = new File(cache.getAbsolutePath() + ".tmp");

                InputStream in = null;
                OutputStream out = null;

                try {
                    URL parsedUrl = new URL(url);
                    retry:
                    for (int i = 0; i < tryTimes && !isCancelled(); i++) {

                        HttpURLConnection connection = Connectivity.openDefaultConnection(parsedUrl,
                                timeoutMs * (1 + i / 2), (timeoutMs * (2 + i)));
                        if (temp.exists()) {
                            connection.addRequestProperty("Range", "bytes=" + temp.length() + "-");
                            out = new FileOutputStream(temp, true);
                        } else
                            out = new FileOutputStream(temp);
                        try {
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200 || responseCode == 206) {
                                in = connection.getInputStream();
                                FileUtil.copyStream(in, out);
                                cache.delete();
                                if (!temp.renameTo(cache)) {
                                    Log.w(TAG, "重命名失败" + temp.getName());
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
                int v = values[0] + 1;
                mCurrentHolder.evaluateJavascript("javascript:(function(){" +
                                "var images = document.getElementsByTagName(\"img\");" +
                                "var img = images[" + v + "];" +
                                "img.src = img.getAttribute(\"loc\");" +
                                "})()",
                        null);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            // 确保所有图片都顺利的显示出来
            isDownloaded = true;
            mCurrentHolder.evaluateJavascript("javascript:(function(){"
                            + "var images = document.getElementsByTagName(\"img\"); "
                            + "for(var i=0;i<images.length;i++){"
                            + "var imgSrc = images[i].getAttribute(\"loc\"); "
                            + "if(imgSrc != null)"
                            + "images[i].setAttribute(\"src\",imgSrc);"
                            + "}"
                            + "})()",
                    null);
        }

    }

    /**
     * 异步下载图片到缓存目录
     */
    private class LoadImageTask extends AsyncTask<String, Integer, Void> {

        int timeoutMs = 3000;
        int tryTimes = 3;
        private String url;
        private int index;

        @Override
        protected Void doInBackground(String... params) {
            url = params[0];
            index = imgUrls.indexOf(url.trim());
            if (isCancelled()) {
                return null;
            }
            File cache = imageCaches.get(index);
            if (cache.exists() && cache.canRead()) {
                // if exists, show image
                publishProgress(index);
                return null;
            } else {
                cache.getParentFile().mkdirs();
            }
            File temp = new File(cache.getAbsolutePath() + ".tmp");

            InputStream in = null;
            OutputStream out = null;

            try {
                URL parsedUrl = new URL(url);
                retry:
                for (int i = 0; i < tryTimes && !isCancelled(); i++) {

                    HttpURLConnection connection = Connectivity.openDefaultConnection(parsedUrl,
                            timeoutMs * (1 + i / 2), (timeoutMs * (2 + i)));
                    if (temp.exists()) {
                        connection.addRequestProperty("Range", "bytes=" + temp.length() + "-");
                        out = new FileOutputStream(temp, true);
                    } else
                        out = new FileOutputStream(temp);
                    try {
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200 || responseCode == 206) {
                            in = connection.getInputStream();
                            FileUtil.copyStream(in, out); // write
                            cache.delete();
                            if (!temp.renameTo(cache)) {
                                Log.w(TAG, "重命名失败" + temp.getName());
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

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (imgUrls != null) {
                if (url == null)
                    return;

                int v = values[0];
                mCurrentHolder.evaluateJavascript("javascript:(function(){" +
                                "var images = document.getElementsByTagName(\"img\");" +
                                "var img = images[" + v + "];" +
                                "img.src = img.getAttribute(\"loc\");" +
                                "})()",
                        null);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            // 确保所有图片都顺利的显示出来
            isDownloaded = true;
            String javaScript = "javascript:(function(){"
                    + "var images = document.getElementsByTagName(\"img\"); "
                    + "var imgSrc = images[" + index + "].getAttribute(\"loc\"); "
                    + "if(imgSrc != null)"
                    + "images[" + index + "].setAttribute(\"src\",imgSrc);"
                    + "})()";
            mCurrentHolder.evaluateJavascript(javaScript, null);
        }

    }

    private class ACJSObject {

        /**
         * 点击查看大图
         */
        @android.webkit.JavascriptInterface
        public void viewImage(String url) {
//            ImagePagerActivity.startCacheImage(ArticleActivity2.this,
//                    (ArrayList<File>) imageCaches,
//                    imgUrls.indexOf(url), aid, title);
            Log.i("viewImage", "  ViewImage");
        }

        /**
         * 点击显示图片
         */
        @android.webkit.JavascriptInterface
        public void showImage(String url, int index) {
            new LoadImageTask().execute(url, String.valueOf(index));
        }
    }

}
