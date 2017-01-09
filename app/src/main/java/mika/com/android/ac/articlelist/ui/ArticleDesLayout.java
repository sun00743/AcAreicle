/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.articlelist.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.acapi.ArticleList;
import mika.com.android.ac.ui.CardIconButton;
import mika.com.android.ac.ui.ImageLayout;
import mika.com.android.ac.ui.TimeActionTextView;
import mika.com.android.ac.util.CheatSheetUtils;
import mika.com.android.ac.util.DateUtils;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewUtils;

/**
 * A LinearLayout that can display a article.
 */
public class ArticleDesLayout extends LinearLayout {

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
    /**
     * 头layout
     */
    @BindView(R.id.item_head)
    RelativeLayout mItemHeadLayou;

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
    //    @BindView(R.id.image_list_layout)
//    FrameLayout mImageListLayout;
//    @BindView(R.id.image_list_description_layout)
//    FrameLayout mImageListDescriptionLayout;
//    @BindView(R.id.image_list_description)
//    TextView mImageListDescriptionText;
//    @BindView(R.id.image_list)
//    RecyclerView mImageList;
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

//    private HorizontalImageAdapter mImageListAdapter;
    /**
     * 时间差
     */
    public String diffTime;

    public ArticleDesLayout(Context context) {
        super(context);

        init();
    }

    public ArticleDesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ArticleDesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleDesLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        setOrientation(VERTICAL);

        ViewUtils.inflateInto(R.layout.articlelist_layout, this);
        ButterKnife.bind(this);
        ViewUtils.setTextViewLinkClickable(mTextText);

        CheatSheetUtils.setup(mStarButton);
        CheatSheetUtils.setup(mCommentButton);
        CheatSheetUtils.setup(mBananaButton);
    }

    public void isInList(boolean inList) {
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
    public void loadAvatarImage(final ArticleList articleList) {
        ImageUtils.loadAvatar(mAvatarImage, articleList.userAvatar);
    }

    public void cleanAvatarImage() {
        mAvatarImage.setImageResource(R.drawable.avatar_icon_grey600_40dp);
    }

    /**
     * 绑定articleDes并且设置事件监听和展示信息
     *
     * @param articleList
     */
    public void bindArticleList(final ArticleList articleList) {

        final Context context = getContext();
        mAvatarImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.avatar_icon_grey600_40dp));

        //加载图片
        ImageUtils.loadAvatar(mAvatarImage, articleList.userAvatar);
        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                    context.startActivity(ProfileActivity.makeIntent(broadcast.author, context));
                // todo 打开user的个人资料
            }
        });
        mNameText.setText(articleList.username);
//        mTimeFormatText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);
        mTimeFormatText.setText(calculateTimeDiff(articleList));
//        mCommentCount.setText(articleList.commentCount + " 评论");
        mWatchCount.setText(articleList.viewCount + " " + "围观");

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
            if (mInList || TextUtils.isEmpty(articleUrl)) {
                //内容区域点击事件
                mAttachmentLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onInListClicked();
                        }
                    }
                });
            } else {
                mAttachmentLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                                UriHandler.open(articleUrl, context);
                    }
                });
            }
            mSingleImageLayout.setVisibility(GONE);
//            mImageListLayout.setVisibility(GONE);
            ViewUtils.setVisibleOrGone(mTextSpace, false);
        }

//        mLikeButton.setText(broadcast.getLikeCountString());
        // todo 收藏按钮
        /*LikeBroadcastManager likeBroadcastManager = LikeBroadcastManager.getInstance();
        if (likeBroadcastManager.isWriting(articleList.id)) {
            mLikeButton.setActivated(likeBroadcastManager.isWritingLike(broadcast.id));
            mLikeButton.setEnabled(false);
        } else {
            mLikeButton.setActivated(broadcast.isLiked);
            mLikeButton.setEnabled(true);
        }*/
        mStarButton.setText(String.valueOf(articleList.favoriteCount));
        mStarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onStarClicked();
                }
            }
        });
        // todo 香蕉按钮 或者 分享按钮吧
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

    public View getItemHeadLayout() {
        return mItemHeadLayou;
    }

    /**
     * 计算时间差
     *
     * @param articleList 文章列表
     * @return 时间差 like "xx分钟前"
     */
    private String calculateTimeDiff(ArticleList articleList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long diff = 0;
        try {
            diff = new Date(System.currentTimeMillis()).getTime()
                    - format.parse(articleList.contributeTimeFormat).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String agoTime = DateUtils.formatAgoTimes(diff);
        articleList.diffTime = agoTime;
        return agoTime;
    }

    public void releaseArticleList() {
        mAvatarImage.setImageDrawable(null);
        mAttachmentImage.setImageDrawable(null);
        mSingleImageLayout.releaseImage();
//        mImageListAdapter.clear();
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
