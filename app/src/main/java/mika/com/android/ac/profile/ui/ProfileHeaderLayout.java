/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import mika.com.android.ac.R;
import mika.com.android.ac.followship.content.FollowUserManager;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.acapi.Mention;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.profile.util.ProfileUtils;
import mika.com.android.ac.ui.FlexibleSpaceHeaderView;
import mika.com.android.ac.ui.GalleryActivity;
import mika.com.android.ac.ui.JoinedAtLocationAutoGoneTextView;
import mika.com.android.ac.ui.WhiteIndeterminateProgressIconDrawable;
import mika.com.android.ac.util.AppUtils;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.MathUtils;
import mika.com.android.ac.util.StatusBarColorUtils;
import mika.com.android.ac.util.ViewUtils;

/**
 * Set the initial layout_height to match_parent or wrap_content instead a specific value so that
 * the view measures itself correctly for the first time.
 */
public class ProfileHeaderLayout extends FrameLayout implements FlexibleSpaceHeaderView {

    @BindColor(R.color.system_window_scrim)
    int mStatusBarColorScrim;
    private int mStatusBarColorFullscreen;
    @BindDimen(R.dimen.profile_large_avatar_size1)
    int mLargeAvatarSize;
    @BindDimen(mika.com.android.ac.R.dimen.profile_small_avatar_size)
    int mSmallAvatarSize;
    @BindDimen(R.dimen.screen_edge_horizontal_margin)
    int mScreenEdgeHorizontalMargin;
    @BindDimen(mika.com.android.ac.R.dimen.toolbar_height)
    int mToolbarHeight;

    @BindView(R.id.dismiss)
    View mDismissView;
    @BindView(R.id.appBar)
    LinearLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_username)
    TextView mToolbarUsernameText;
    @BindView(R.id.username)
    TextView mUsernameText;
    @BindView(R.id.signature)
    TextView mSignatureText;
    @BindView(R.id.banana)
    JoinedAtLocationAutoGoneTextView banana;
    @BindView(R.id.follow)
    Button mFollowButton;
    @BindView(R.id.avatar_container)
    FrameLayout mAvatarContainerLayout;
    @BindView(R.id.avatar)
    CircleImageView mAvatarImage;

    private boolean mUseWideLayout;

    private int mInsetTop;
    private int mMaxHeight;
    private int mScroll;

    private Listener mListener;

    public ProfileHeaderLayout(Context context) {
        super(context);

        init();
    }

    public ProfileHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProfileHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr,
                               int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {

        // HACK: We need to delegate the outline so that elevation can work.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    // We cannot use mAppBarLayout.getOutlineProvider.getOutline() because the
                    // bounds of it is not kept in sync when this method is called.
                    // HACK: Workaround the fact that we must provided an outline before we are
                    // measured.
                    int height = getHeight();
                    int top = height > 0 ? height - computeVisibleAppBarHeight() : 0;
                    outline.setRect(0, top, getWidth(), height);
                }
            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
        Context context = getContext();
        mStatusBarColorFullscreen = ViewUtils.getColorFromAttrRes(R.attr.colorPrimaryDark, 0,
                context);
        mUseWideLayout = ProfileUtils.shouldUseWideLayout(context);

        StatusBarColorUtils.set(mStatusBarColorScrim, (Activity) context);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY);
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int dismissViewHeight = height - computeVisibleAppBarHeight();
        mDismissView.getLayoutParams().height = dismissViewHeight;

        MarginLayoutParams appBarLayoutLayoutParams =
                (MarginLayoutParams) mAppBarLayout.getLayoutParams();
        appBarLayoutLayoutParams.topMargin = dismissViewHeight;
        // So that the layout remains stable.
        appBarLayoutLayoutParams.height = getAppBarMaxHeight();
        int appBarWidth = ProfileUtils.getAppBarWidth(width, getContext());
        if (mUseWideLayout) {
            mAppBarLayout.setPadding(mAppBarLayout.getPaddingLeft(), mAppBarLayout.getPaddingTop(),
                    width - appBarWidth, mAppBarLayout.getPaddingBottom());
        }

        int largeAvatarSizeHalf = mLargeAvatarSize / 2;
        int avatarMarginTop = dismissViewHeight - mInsetTop - largeAvatarSizeHalf;
        int smallAvatarMarginTop = (mToolbarHeight - mSmallAvatarSize) / 2;
        float avatarHorizontalFraction = avatarMarginTop < smallAvatarMarginTop ?
                MathUtils.unlerp(smallAvatarMarginTop, -largeAvatarSizeHalf, avatarMarginTop) : 0;
        avatarMarginTop = Math.max(smallAvatarMarginTop, avatarMarginTop) + mInsetTop;
        int avatarMarginLeft = MathUtils.lerp(appBarWidth / 2 - largeAvatarSizeHalf,
                mScreenEdgeHorizontalMargin, avatarHorizontalFraction);
        MarginLayoutParams avatarContainerLayoutParams =
                (MarginLayoutParams) mAvatarContainerLayout.getLayoutParams();
        avatarContainerLayoutParams.leftMargin = avatarMarginLeft;
        avatarContainerLayoutParams.topMargin = avatarMarginTop;
        float avatarScale = MathUtils.lerp(1, (float) mSmallAvatarSize / mLargeAvatarSize,
                avatarHorizontalFraction);
        mAvatarContainerLayout.setPivotX(0);
        mAvatarContainerLayout.setPivotY(0);
        mAvatarContainerLayout.setScaleX(avatarScale);
        mAvatarContainerLayout.setScaleY(avatarScale);

        for (int i = 0, count = mAppBarLayout.getChildCount(); i < count; ++i) {
            View child = mAppBarLayout.getChildAt(i);
            if (child != mToolbar) {
                child.setAlpha(Math.max(0, 1 - getFraction() * 2));
            }
        }
        mToolbarUsernameText.setAlpha(Math.max(0, getFraction() * 2 - 1));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setInsetTop(int insetTop) {
        if (mInsetTop == insetTop) {
            return;
        }
        mInsetTop = insetTop;
        requestLayout();
    }

    private int getAppBarMinHeight() {
        if (mUseWideLayout) {
            return getAppBarMaxHeight();
        } else {
            // So that we don't need to wait until measure.
            return mToolbar.getLayoutParams().height;
        }
    }

    private int getAppBarMaxHeight() {
        return mUseWideLayout ? mMaxHeight * 3 / 5 : mMaxHeight / 2;
    }

    private int computeVisibleAppBarHeight() {
        return MathUtils.lerp(getAppBarMaxHeight(), getAppBarMinHeight(), getFraction());
    }

    private float getFraction() {
        int scrollExtent = getScrollExtent();
        return scrollExtent > 0 ? (float) mScroll / scrollExtent : 0;
    }

    @Override
    public int getScroll() {
        return mScroll;
    }

    @Override
    public int getScrollExtent() {
        return mMaxHeight - getMinHeight();
    }

    @Override
    public void scrollTo(int scroll) {

        int scrollExtent = getScrollExtent();
        scroll = MathUtils.clamp(scroll, 0, scrollExtent);
        if (mScroll == scroll) {
            return;
        }

        ViewUtils.setHeight(this, mMaxHeight - scroll);
        int oldScroll = mScroll;
        mScroll = scroll;

        if (oldScroll < scrollExtent && mScroll == scrollExtent) {
            StatusBarColorUtils.animateTo(mStatusBarColorFullscreen,
                    AppUtils.getActivityFromContext(getContext()));
        } else if (oldScroll == scrollExtent && mScroll < oldScroll) {
            StatusBarColorUtils.animateTo(mStatusBarColorScrim,
                    AppUtils.getActivityFromContext(getContext()));
        }
    }

    private int getMinHeight() {
        if (mUseWideLayout) {
            return mMaxHeight;
        } else {
            // So that we don't need to wait until measure.
            return mToolbar.getLayoutParams().height + mInsetTop;
        }
    }

    // Should be called by ProfileLayout.onMeasure() before its super call.
    public void setMaxHeight(int maxHeight) {

        if (mMaxHeight == maxHeight) {
            return;
        }

        mMaxHeight = maxHeight;
        ViewUtils.setHeight(mAppBarLayout, mMaxHeight);
    }

    public void bindUser(User user) {
        final String largeAvatar = user.getLargeAvatarOrAvatar();
        ImageUtils.loadProfileAvatarAndFadeIn(mAvatarImage, largeAvatar);
        final Context context = getContext();
        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(GalleryActivity.makeIntent(largeAvatar, context));
            }
        });
        mToolbarUsernameText.setText(user.name);
        mUsernameText.setText(user.name);
        mSignatureText.setText(null);
        banana.setText(null);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mFollowButton, 0, 0, 0, 0);
        mFollowButton.setVisibility(GONE);
    }

    public void bindUserInfo(final UserInfo userInfo) {
        final Context context = getContext();
        if (!ViewUtils.isVisible(mAvatarImage)) {
            // HACK: Don't load avatar again if already loaded by bindUser().
            final String largeAvatar = userInfo.getLargeAvatarOrAvatar();
            ImageUtils.loadProfileAvatarAndFadeIn(mAvatarImage, largeAvatar);
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(GalleryActivity.makeIntent(largeAvatar, context));
                }
            });
        }
        mToolbarUsernameText.setText(userInfo.name);
        mUsernameText.setText(userInfo.name);
        mSignatureText.setText(userInfo.signature);
        banana.setJoinedAtAndLocation(userInfo.createdAt, userInfo.locationName);
        if (userInfo.isOneself(context)) {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mFollowButton,
                    R.drawable.edit_icon_white_24dp, 0, 0, 0);
            mFollowButton.setText(R.string.profile_edit);
            mFollowButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onEditProfile(userInfo);
                    }
                }
            });
        } else {
            FollowUserManager followUserManager = FollowUserManager.getInstance();
            String userIdOrUid = userInfo.getIdOrUid();
            if (followUserManager.isWriting(userIdOrUid)) {
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mFollowButton,
                        new WhiteIndeterminateProgressIconDrawable(context), null, null, null);
                mFollowButton.setText(followUserManager.isWritingFollow(userIdOrUid) ?
                        R.string.user_following : R.string.user_unfollowing);
            } else {
                int followDrawableId;
                int followStringId;
                if (userInfo.isFollowed) {
                    if (userInfo.isFollower) {
                        followDrawableId = R.drawable.mutual_icon_white_24dp;
                        followStringId = R.string.profile_following_mutual;
                    } else {
                        followDrawableId = R.drawable.ok_icon_white_24dp;
                        followStringId = R.string.profile_following;
                    }
                } else {
                    followDrawableId = R.drawable.add_icon_white_24dp;
                    followStringId = R.string.profile_follow;
                }
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mFollowButton,
                        followDrawableId, 0, 0, 0);
                mFollowButton.setText(followStringId);
            }
            mFollowButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onFollowUser(userInfo, !userInfo.isFollowed);
                    }
                }
            });
        }
        mFollowButton.setVisibility(VISIBLE);
    }

    public void bindAcerInfo(@NonNull final AcerInfo2 acerInfo) {
        final Context context = getContext();
        if (!ViewUtils.isVisible(mAvatarImage)) {
            // HACK: Don't load avatar again if already loaded by bindUser().
            final String largeAvatar = acerInfo.avatar;
            ImageUtils.loadProfileAvatarAndFadeIn(mAvatarImage, largeAvatar);
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(GalleryActivity.makeIntent(largeAvatar, context));
                }
            });
        }
        mToolbarUsernameText.setText(acerInfo.name);
        mUsernameText.setText(acerInfo.name);
        mSignatureText.setMovementMethod(ScrollingMovementMethod.getInstance());
        mSignatureText.setText(acerInfo.sign);
        ApiRequest<Mention> request = ApiRequests.newPushRequest();
        if (request == null){
            return;
        }
        Volley.getInstance().addToRequestQueue(request.setListener(new Response.Listener<Mention>() {
            @Override
            public void onResponse(Mention response) {
                banana.setText(String.format(Locale.getDefault(), context.getString(R.string.profile_head_count),
                        response.mention, response.unReadMail, response.newFollowed, response.newPush));
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                banana.setText(context.getString(R.string.normal_request_error));
            }
        }));
    }

    public interface Listener {
        void onEditProfile(UserInfo userInfo);

        void onFollowUser(UserInfo userInfo, boolean follow);
    }
}
