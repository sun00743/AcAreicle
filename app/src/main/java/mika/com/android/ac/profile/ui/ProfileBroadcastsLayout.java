/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.broadcast.ui.BroadcastActivity;
import mika.com.android.ac.broadcast.ui.BroadcastListActivity;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.Image;
import mika.com.android.ac.network.api.info.apiv2.Photo;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.ui.FriendlyCardView;
import mika.com.android.ac.ui.TimeActionTextView;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewUtils;

public class ProfileBroadcastsLayout extends FriendlyCardView {

    private static final int BROADCAST_COUNT_MAX = 3;

    @BindView(mika.com.android.ac.R.id.title)
    TextView mTitleText;
    @BindView(mika.com.android.ac.R.id.broadcast_list)
    LinearLayout mBroadcastList;
    @BindView(mika.com.android.ac.R.id.empty)
    View mEmptyView;
    @BindView(mika.com.android.ac.R.id.view_more)
    TextView mViewMoreText;

    public ProfileBroadcastsLayout(Context context) {
        super(context);

        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        ViewUtils.inflateInto(mika.com.android.ac.R.layout.profile_broadcasts_layout, this);
        ButterKnife.bind(this);
    }

    public void bind(final UserInfo userInfo, List<Broadcast> broadcastList) {

        final Context context = getContext();
        View.OnClickListener viewMoreListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(BroadcastListActivity.makeIntent(userInfo, context));
            }
        };
        mTitleText.setOnClickListener(viewMoreListener);
        mViewMoreText.setOnClickListener(viewMoreListener);

        int i = 0;
        for (final Broadcast broadcast : broadcastList) {

            if (i >= BROADCAST_COUNT_MAX) {
                break;
            }

            if (broadcast.rebroadcastedBroadcast != null) {
                continue;
            }

            if (i >= mBroadcastList.getChildCount()) {
                ViewUtils.inflateInto(mika.com.android.ac.R.layout.profile_broadcast_item, mBroadcastList);
            }
            View broadcastLayout = mBroadcastList.getChildAt(i);
            broadcastLayout.setVisibility(VISIBLE);
            BroadcastLayoutHolder holder = (BroadcastLayoutHolder) broadcastLayout.getTag();
            if (holder == null) {
                holder = new BroadcastLayoutHolder(broadcastLayout);
                broadcastLayout.setTag(holder);
                ViewUtils.setTextViewLinkClickable(holder.textText);
            }

            // HACK: Should not change on rebind.
            if (holder.boundBroadcastId != broadcast.id) {
                String imageUrl = null;
                if (broadcast.attachment != null) {
                    imageUrl = broadcast.attachment.image;
                }
                if (TextUtils.isEmpty(imageUrl)) {
                    List<Image> images = broadcast.images.size() > 0 ? broadcast.images
                            : Photo.toImageList(broadcast.photos);
                    if (images.size() > 0){
                        imageUrl = images.get(0).medium;
                    }
                }
                if (!TextUtils.isEmpty(imageUrl)) {
                    holder.image.setVisibility(VISIBLE);
                    ImageUtils.loadImage(holder.image, imageUrl);
                } else {
                    holder.image.setVisibility(GONE);
                }
                CharSequence text = broadcast.getTextWithEntities(context);
                if (TextUtils.isEmpty(text) && broadcast.attachment != null) {
                    text = broadcast.attachment.title;
                }
                holder.textText.setText(text);
                holder.timeActionText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);
                broadcastLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(BroadcastActivity.makeIntent(broadcast, context));
                    }
                });
                holder.boundBroadcastId = broadcast.id;
            }

            ++i;
        }

        ViewUtils.setVisibleOrGone(mBroadcastList, i != 0);
        ViewUtils.setVisibleOrGone(mEmptyView, i == 0);

        if (userInfo.broadcastCount > i) {
            mViewMoreText.setText(context.getString(mika.com.android.ac.R.string.view_more_with_count_format,
                    userInfo.broadcastCount));
        } else {
            mViewMoreText.setVisibility(GONE);
        }

        for (int count = mBroadcastList.getChildCount(); i < count; ++i) {
            mBroadcastList.getChildAt(i).setVisibility(GONE);
        }
    }

    static class BroadcastLayoutHolder {

        @BindView(mika.com.android.ac.R.id.image)
        public ImageView image;
        @BindView(mika.com.android.ac.R.id.text)
        public TextView textText;
        @BindView(mika.com.android.ac.R.id.time_action)
        public TimeActionTextView timeActionText;
        public long boundBroadcastId;

        public BroadcastLayoutHolder(View broadcastLayout) {
            ButterKnife.bind(this, broadcastLayout);
        }
    }
}
