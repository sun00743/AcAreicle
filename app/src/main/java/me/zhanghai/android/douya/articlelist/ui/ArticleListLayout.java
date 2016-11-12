/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.articlelist.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.network.api.info.acapi.ArticleList;
import me.zhanghai.android.douya.ui.CardIconButton;
import me.zhanghai.android.douya.ui.HorizontalImageAdapter;
import me.zhanghai.android.douya.ui.ImageLayout;
import me.zhanghai.android.douya.ui.OnHorizontalScrollListener;
import me.zhanghai.android.douya.ui.TimeActionTextView;
import me.zhanghai.android.douya.util.CheatSheetUtils;
import me.zhanghai.android.douya.util.DrawableUtils;
import me.zhanghai.android.douya.util.ImageUtils;
import me.zhanghai.android.douya.util.ViewCompat;
import me.zhanghai.android.douya.util.ViewUtils;

/**
 * A LinearLayout that can display a article.
 * by mika
 *
 * <p>Note that this layout tries to avoid the glitch if the same broadcast is bound again by
 * leaving attachment and text unchanged (since they cannot change once a broadcast is created).</p>
 */
public class ArticleListLayout extends LinearLayout {

    /**
     * up头像
     */
    @BindView(R.id.user_avatar)
    ImageView mAvatarImage;
    /**
     * up
     */
    @BindView(R.id.user_name)
    TextView mNameText;
    /**
     * 投稿时间
     */
    @BindView(R.id.time_format)
    TimeActionTextView mTimeFormatText;
    /**
     * 评论数
     */
    @BindView(R.id.comment_count)
    TextView mCommentCount;
    /**
     * 围观数
     */
    @BindView(R.id.view_count)
    TextView mWatchCount;

    @BindView(R.id.attachment)
    RelativeLayout mAttachmentLayout;
    @BindView(R.id.attachment_image)
    ImageView mAttachmentImage;
    /**
     * 文章题目
     */
    @BindView(R.id.article_title)
    TextView mArticleTitleText;
    /**
     * 文章概述
     */
    @BindView(R.id.article_description)
    TextView mArticleDescriptionText;
    @BindView(R.id.single_image)
    ImageLayout mSingleImageLayout;
    @BindView(R.id.image_list_layout)
    FrameLayout mImageListLayout;
    @BindView(R.id.image_list_description_layout)
    FrameLayout mImageListDescriptionLayout;
    @BindView(R.id.image_list_description)
    TextView mImageListDescriptionText;
    @BindView(R.id.image_list)
    RecyclerView mImageList;
    @BindView(R.id.text_space)
    Space mTextSpace;
    @BindView(R.id.text)
    TextView mTextText;

    @BindView(R.id.art_star)
    CardIconButton mStarButton;
    @BindView(R.id.art_comment)
    CardIconButton mCommentButton;
    @BindView(R.id.art_banana)
    CardIconButton mBananaButton;

    //判断是否在recycleView中
    private boolean mInList = false;
//    private InListClickListener mInListClickListener;
//    private BroadcastAdapter mAdapter = null;

    private OnBtnClickedListener mListener;

    private long mBoundArticleId;

    private HorizontalImageAdapter mImageListAdapter;
    /**
     * 时间差
     */
    public String diffTime;

    public ArticleListLayout(Context context) {
        super(context);

        init();
    }

    public ArticleListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ArticleListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        setOrientation(VERTICAL);

        ViewUtils.inflateInto(R.layout.articlelist_layout, this);
        ButterKnife.bind(this);

        ViewCompat.setBackground(mImageListDescriptionLayout, DrawableUtils.makeScrimDrawable());
        mImageList.setHasFixedSize(true);
        mImageList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mImageListAdapter = new HorizontalImageAdapter();
        mImageList.setAdapter(mImageListAdapter);
        mImageList.addOnScrollListener(new OnHorizontalScrollListener() {

            private boolean mShowingDescription = true;

            @Override
            public void onScrolledLeft() {
                if (!mShowingDescription) {
                    mShowingDescription = true;
                    ViewUtils.fadeIn(mImageListDescriptionLayout);
                }
            }

            @Override
            public void onScrolledRight() {
                if (mShowingDescription) {
                    mShowingDescription = false;
                    ViewUtils.fadeOut(mImageListDescriptionLayout);
                }
            }
        });

        ViewUtils.setTextViewLinkClickable(mTextText);

        CheatSheetUtils.setup(mStarButton);
        CheatSheetUtils.setup(mCommentButton);
        CheatSheetUtils.setup(mBananaButton);
    }

    public void isInList(boolean inList){
        mInList = inList;
    }

//    public void setInListClickListener(InListClickListener inListClickListener){
//        mInListClickListener = inListClickListener;
//    }

//    public void setCurrentAdapter(BroadcastAdapter adapter){
//        mAdapter = adapter;
//    }

    public void setOnBtnClickedListener(OnBtnClickedListener listener) {
        mListener = listener;
    }

    /**
     * 加载up头像
     */
    public void loadAvatarImage(final ArticleList articleList){
        Log.i("lllllllll" , articleList.title);
        ImageUtils.loadAvatar(mAvatarImage, articleList.userAvatar);
    }

    public void cleanAvatarImage(){
        Log.i("ccccccccc" , mArticleTitleText.getText().toString());
        mAvatarImage.setImageResource(R.drawable.avatar_icon_grey600_40dp);
    }

    /**
     * 绑定articleDes并且设置事件监听和展示信息
     * @param articleList
     */
    public void bindArticleList(final ArticleList articleList) {

        final Context context = getContext();

//        if (broadcast.isInterest) {
        mAvatarImage.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.avatar_icon_grey600_40dp));
//            mAvatarImage.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
                    // FIXME
//                    UriHandler.open("https://www.douban.com/interest/1/1/", context);
//                }
//            });
//        } else {
            //加载图片
        ImageUtils.loadAvatar(mAvatarImage, articleList.userAvatar);
        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                    context.startActivity(ProfileActivity.makeIntent(broadcast.author, context));
                    //打开user的个人资料
            }
        });
//        }
        mNameText.setText(articleList.username);
//        mTimeFormatText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);
        mTimeFormatText.setText(calculateTimeDiff(articleList));
//        mCommentCount.setText(articleList.commentCount + " 评论");
        mWatchCount.setText(articleList.viewCount + " 围观");

        boolean isRebind = mBoundArticleId != 0 && mBoundArticleId == articleList.id;
        // HACK: Attachment and text should not change on rebind.
        if (!isRebind) {

            //显示内容区域
                mAttachmentLayout.setVisibility(VISIBLE);
            mArticleTitleText.setText(articleList.title);
            mArticleDescriptionText.setText(articleList.description);
//                if (!TextUtils.isEmpty(articleList.image)) {
//                    mAttachmentImage.setVisibility(VISIBLE);
//                    ImageUtils.loadImage(mAttachmentImage, articleList.image);
//                } else {
                    mAttachmentImage.setVisibility(GONE);
//                }
                final String articleUrl = articleList.link;
                //是否在recycleView中
                if(mInList || TextUtils.isEmpty(articleUrl)){
                    //内容区域点击事件
                    mAttachmentLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mListener != null){
                                mListener.onInListClicked();
                            }
                        }
                    });
                }else{
                    mAttachmentLayout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                UriHandler.open(articleUrl, context);
                            }
                        });
                }

//                    if (!TextUtils.isEmpty(attachmentUrl)) {
//                        mAttachmentLayout.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                UriHandler.open(attachmentUrl, context);
//                            }
//                        });
//                    } else {
//                        mAttachmentLayout.setOnClickListener(null);
//                    }

//            } else {
//                mAttachmentLayout.setVisibility(GONE);
//            }

        //图片列表相关
//            final ArrayList<Image> images = broadcast.images.size() > 0 ? broadcast.images
//                    : Photo.toImageList(broadcast.photos);
//            int numImages = images.size();
//            if (numImages == 1) {
//                final Image image = images.get(0);
//                mSingleImageLayout.setVisibility(VISIBLE);
//                mSingleImageLayout.loadImage(image);
//                mSingleImageLayout.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        context.startActivity(GalleryActivity.makeIntent(image, context));
//                    }
//                });
//            } else {
                mSingleImageLayout.setVisibility(GONE);
//            }
//            if (numImages > 1) {
//                mImageListLayout.setVisibility(VISIBLE);
//                mImageListDescriptionText.setText(context.getString(
//                        R.string.broadcast_image_list_count_format, numImages));
//                mImageListAdapter.replace(images);
//                mImageListAdapter.setOnImageClickListener(
//                        new HorizontalImageAdapter.OnImageClickListener() {
//                            @Override
//                            public void onImageClick(int position) {
//                                context.startActivity(GalleryActivity.makeImageListIntent(images,
//                                        position, context));
//                            }
//                        });
//            } else {
                mImageListLayout.setVisibility(GONE);
//            }

        //
//            boolean textSpaceVisible = (articleList != null || numImages > 0)
//                    && !TextUtils.isEmpty(broadcast.text);
            ViewUtils.setVisibleOrGone(mTextSpace, false);
//            mTextText.setText(broadcast.getTextWithEntities(context));
        }

//        mLikeButton.setText(broadcast.getLikeCountString());
        //收藏按钮
        /*LikeBroadcastManager likeBroadcastManager = LikeBroadcastManager.getInstance();
        if (likeBroadcastManager.isWriting(articleList.id)) {
            mLikeButton.setActivated(likeBroadcastManager.isWritingLike(broadcast.id));
            mLikeButton.setEnabled(false);
        } else {
            mLikeButton.setActivated(broadcast.isLiked);
            mLikeButton.setEnabled(true);
        }*/
        mStarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onStarClicked();
                }
            }
        });
        //香蕉按钮
        /*RebroadcastBroadcastManager rebroadcastBroadcastManager =
                RebroadcastBroadcastManager.getInstance();
        if (rebroadcastBroadcastManager.isWriting(broadcast.id)) {
            mRebroadcastButton.setActivated(rebroadcastBroadcastManager.isWritingRebroadcast(
                    broadcast.id));
            mRebroadcastButton.setEnabled(false);
        } else {
            mRebroadcastButton.setActivated(broadcast.isRebroadcasted());
            mRebroadcastButton.setEnabled(true);
        }*/
        mBananaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onBananaClicked();
                }
            }
        });
        mCommentButton.setText(String.valueOf(articleList.commentCount));
        mCommentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCommentClicked();
                }
            }
        });

        mBoundArticleId = articleList.id;
    }

    /**
     * 计算时间差
     * @param articleList
     * @return
     */
    private String calculateTimeDiff(ArticleList articleList){
//        if(diffTime != null)
//            return diffTime;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            diff = new Date(System.currentTimeMillis()).getTime()
                    - format.parse(articleList.contributeTimeFormat).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = (diff/(1000*60*60*24));
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        String agoTime;
        if (days <= 0){
            if(hours <= 0){
                agoTime = minutes + " 分钟前";
            }else{
                agoTime = hours + " 小时前";
            }
        }else{
            agoTime = days + " 天前";
        }
        articleList.diffTime = agoTime;
        return agoTime;
    }

    public void releaseArticleList() {
        mAvatarImage.setImageDrawable(null);
        mAttachmentImage.setImageDrawable(null);
        mSingleImageLayout.releaseImage();
        mImageListAdapter.clear();
        mBoundArticleId = 0;
    }

    /**
     * 点击事件监听
     */
    public interface OnBtnClickedListener {
        void onStarClicked();
        void onBananaClicked();
        void onCommentClicked();

        /**
         * 在recyclerView中被点击
         */
        void onInListClicked();
    }
}