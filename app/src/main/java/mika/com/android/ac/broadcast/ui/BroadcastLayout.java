/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.broadcast.content.LikeBroadcastManager;
import mika.com.android.ac.broadcast.content.RebroadcastBroadcastManager;
import mika.com.android.ac.link.UriHandler;
import mika.com.android.ac.network.api.info.apiv2.Attachment;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.Image;
import mika.com.android.ac.network.api.info.apiv2.Photo;
import mika.com.android.ac.profile.ui.ProfileActivity;
import mika.com.android.ac.ui.CardIconButton;
import mika.com.android.ac.ui.GalleryActivity;
import mika.com.android.ac.ui.HorizontalImageAdapter;
import mika.com.android.ac.ui.ImageLayout;
import mika.com.android.ac.ui.OnHorizontalScrollListener;
import mika.com.android.ac.ui.TimeActionTextView;
import mika.com.android.ac.util.CheatSheetUtils;
import mika.com.android.ac.util.DrawableUtils;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewCompat;
import mika.com.android.ac.util.ViewUtils;

/**
 * A LinearLayout that can display a broadcast.
 *
 * <p>Note that this layout tries to avoid the glitch if the same broadcast is bound again by
 * leaving attachment and text unchanged (since they cannot change once a broadcast is created).</p>
 */
public class BroadcastLayout extends LinearLayout {

    @BindView(mika.com.android.ac.R.id.avatar)
    ImageView mAvatarImage;
    @BindView(mika.com.android.ac.R.id.name)
    TextView mNameText;
    @BindView(mika.com.android.ac.R.id.time_action)
    TimeActionTextView mTimeActionText;
    @BindView(mika.com.android.ac.R.id.attachment)
    RelativeLayout mAttachmentLayout;
    @BindView(mika.com.android.ac.R.id.attachment_image)
    ImageView mAttachmentImage;
    @BindView(mika.com.android.ac.R.id.attachment_title)
    TextView mAttachmentTitleText;
    @BindView(mika.com.android.ac.R.id.attachment_description)
    TextView mAttachmentDescriptionText;
    @BindView(mika.com.android.ac.R.id.single_image)
    ImageLayout mSingleImageLayout;
    @BindView(mika.com.android.ac.R.id.image_list_layout)
    FrameLayout mImageListLayout;
    @BindView(mika.com.android.ac.R.id.image_list_description_layout)
    FrameLayout mImageListDescriptionLayout;
    @BindView(mika.com.android.ac.R.id.image_list_description)
    TextView mImageListDescriptionText;
    @BindView(mika.com.android.ac.R.id.image_list)
    RecyclerView mImageList;
    @BindView(mika.com.android.ac.R.id.text_space)
    Space mTextSpace;
    @BindView(mika.com.android.ac.R.id.text)
    TextView mTextText;
    @BindView(mika.com.android.ac.R.id.like)
    CardIconButton mLikeButton;
    @BindView(mika.com.android.ac.R.id.comment)
    CardIconButton mCommentButton;
    @BindView(mika.com.android.ac.R.id.rebroadcast)
    CardIconButton mRebroadcastButton;

    //判断是否在recycleView中
    private boolean mInList = false;
//    private InListClickListener mInListClickListener;
//    private BroadcastAdapter mAdapter = null;

    private Listener mListener;

    private Long mBoundBroadcastId;

    private HorizontalImageAdapter mImageListAdapter;

    public BroadcastLayout(Context context) {
        super(context);

        init();
    }

    public BroadcastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        setOrientation(VERTICAL);

        ViewUtils.inflateInto(mika.com.android.ac.R.layout.broadcast_layout, this);
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

        CheatSheetUtils.setup(mLikeButton);
        CheatSheetUtils.setup(mCommentButton);
        CheatSheetUtils.setup(mRebroadcastButton);
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

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void bindBroadcast(final Broadcast broadcast) {

        final Context context = getContext();

        if (broadcast.isInterest) {
            mAvatarImage.setImageDrawable(ContextCompat.getDrawable(context,
                    mika.com.android.ac.R.drawable.recommendation_avatar_icon_grey600_40dp));
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // FIXME
                    UriHandler.open("https://www.douban.com/interest/1/1/", context);
                }
            });
        } else {
            ImageUtils.loadAvatar(mAvatarImage, broadcast.author.avatar);
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(ProfileActivity.makeIntent(broadcast.author, context));
                }
            });
        }
        mNameText.setText(broadcast.getAuthorName());
        mTimeActionText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);

        boolean isRebind = mBoundBroadcastId != null && mBoundBroadcastId == broadcast.id;
        // HACK: Attachment and text should not change on rebind.
        if (!isRebind) {

            Attachment attachment = broadcast.attachment;
            if (attachment != null) {
                mAttachmentLayout.setVisibility(VISIBLE);
                mAttachmentTitleText.setText(attachment.title);
                mAttachmentDescriptionText.setText(attachment.description);
                if (!TextUtils.isEmpty(attachment.image)) {
                    mAttachmentImage.setVisibility(VISIBLE);
                    ImageUtils.loadImage(mAttachmentImage, attachment.image);
                } else {
                    mAttachmentImage.setVisibility(GONE);
                }
                final String attachmentUrl = attachment.href;
                //是否在recycleView中
                if(mInList || TextUtils.isEmpty(attachmentUrl)){
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
                                UriHandler.open(attachmentUrl, context);
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

            } else {
                mAttachmentLayout.setVisibility(GONE);
            }

            final ArrayList<Image> images = broadcast.images.size() > 0 ? broadcast.images
                    : Photo.toImageList(broadcast.photos);
            int numImages = images.size();
            if (numImages == 1) {
                final Image image = images.get(0);
                mSingleImageLayout.setVisibility(VISIBLE);
                mSingleImageLayout.loadImage(image);
                mSingleImageLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(GalleryActivity.makeIntent(image, context));
                    }
                });
            } else {
                mSingleImageLayout.setVisibility(GONE);
            }
            if (numImages > 1) {
                mImageListLayout.setVisibility(VISIBLE);
                mImageListDescriptionText.setText(context.getString(
                        mika.com.android.ac.R.string.broadcast_image_list_count_format, numImages));
                mImageListAdapter.replace(images);
                mImageListAdapter.setOnImageClickListener(
                        new HorizontalImageAdapter.OnImageClickListener() {
                            @Override
                            public void onImageClick(int position) {
                                context.startActivity(GalleryActivity.makeImageListIntent(images,
                                        position, context));
                            }
                        });
            } else {
                mImageListLayout.setVisibility(GONE);
            }

            boolean textSpaceVisible = (attachment != null || numImages > 0)
                    && !TextUtils.isEmpty(broadcast.text);
            ViewUtils.setVisibleOrGone(mTextSpace, textSpaceVisible);
            mTextText.setText(broadcast.getTextWithEntities(context));
        }

        mLikeButton.setText(broadcast.getLikeCountString());
        LikeBroadcastManager likeBroadcastManager = LikeBroadcastManager.getInstance();
        if (likeBroadcastManager.isWriting(broadcast.id)) {
            mLikeButton.setActivated(likeBroadcastManager.isWritingLike(broadcast.id));
            mLikeButton.setEnabled(false);
        } else {
            mLikeButton.setActivated(broadcast.isLiked);
            mLikeButton.setEnabled(true);
        }
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeClicked();
                }
            }
        });
        RebroadcastBroadcastManager rebroadcastBroadcastManager =
                RebroadcastBroadcastManager.getInstance();
        if (rebroadcastBroadcastManager.isWriting(broadcast.id)) {
            mRebroadcastButton.setActivated(rebroadcastBroadcastManager.isWritingRebroadcast(
                    broadcast.id));
            mRebroadcastButton.setEnabled(false);
        } else {
            mRebroadcastButton.setActivated(broadcast.isRebroadcasted());
            mRebroadcastButton.setEnabled(true);
        }
        mRebroadcastButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRebroadcastClicked();
                }
            }
        });
        mCommentButton.setText(broadcast.getCommentCountString());
        mCommentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCommentClicked();
                }
            }
        });

        mBoundBroadcastId = broadcast.id;
    }

    public void releaseBroadcast() {
        mAvatarImage.setImageDrawable(null);
        mAttachmentImage.setImageDrawable(null);
        mSingleImageLayout.releaseImage();
        mImageListAdapter.clear();
        mBoundBroadcastId = null;
    }

    /**
     * 点击事件监听
     */
    public interface Listener {
        void onLikeClicked();
        void onRebroadcastClicked();
        void onCommentClicked();

        /**
         * 在recyclerView中被点击
         */
        void onInListClicked();
    }

    public interface InListClickListener {
        void InListClicked();
    }
}
