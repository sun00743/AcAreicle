/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.navigation.ui;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.transitionseverywhere.ChangeTransform;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.ui.CrossfadeText;
import mika.com.android.ac.util.DrawableUtils;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.ViewCompat;
import mika.com.android.ac.util.ViewUtils;

public class NavigationHeaderLayout extends FrameLayout {

    @BindView(mika.com.android.ac.R.id.backdrop)
    ImageView mBackdropImage;
    @BindView(mika.com.android.ac.R.id.scrim)
    View mScrimView;
    @BindViews({mika.com.android.ac.R.id.avatar, mika.com.android.ac.R.id.fade_out_avatar,
            mika.com.android.ac.R.id.recent_one_avatar, mika.com.android.ac.R.id.fade_out_recent_one_avatar,
            mika.com.android.ac.R.id.recent_two_avatar, mika.com.android.ac.R.id.fade_out_recent_two_avatar})
    ImageView[] mAvatarImages;
    @BindView(mika.com.android.ac.R.id.avatar)
    ImageView mAvatarImage;
    @BindView(mika.com.android.ac.R.id.fade_out_avatar)
    ImageView mFadeOutAvatarImage;
    @BindView(mika.com.android.ac.R.id.recent_one_avatar)
    ImageView mRecentOneAvatarImage;
    @BindView(mika.com.android.ac.R.id.fade_out_recent_one_avatar)
    ImageView mFadeOutRecentOneAvatarImage;
    @BindView(mika.com.android.ac.R.id.recent_two_avatar)
    ImageView mRecentTwoAvatarImage;
    @BindView(mika.com.android.ac.R.id.fade_out_recent_two_avatar)
    ImageView mFadeOutRecentTwoAvatarImage;
    @BindView(mika.com.android.ac.R.id.info)
    LinearLayout mInfoLayout;
    @BindView(mika.com.android.ac.R.id.name)
    TextView mNameText;
    @BindView(mika.com.android.ac.R.id.description)
    TextView mDescriptionText;
    @BindView(mika.com.android.ac.R.id.dropDown)
    ImageView mDropDownImage;

    private Adapter mAdapter;
    private Listener mListener;

//    private Account mActiveAccount;
//    private Account mRecentOneAccount;
//    private Account mRecentTwoAccount;

    private boolean mAccountTransitionRunning;
    private boolean mShowingAccountList;

    public NavigationHeaderLayout(Context context) {
        super(context);

        init();
    }

    public NavigationHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NavigationHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                  int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        ViewUtils.inflateInto(mika.com.android.ac.R.layout.navigation_header_layout, this);
        ButterKnife.bind(this);

        mBackdropImage.setImageResource(mika.com.android.ac.R.drawable.profile_header_backdrop);
        ViewCompat.setBackground(mScrimView, DrawableUtils.makeScrimDrawable());
        mInfoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showingAccountList(!mShowingAccountList);
            }
        });
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void bindAcer(AcerInfo2 acerInfo) {

        if (mAdapter == null) {
            return;
        }

        bindAcerInfo(acerInfo);
    }

    private void bindAcerInfo(AcerInfo2 acerInfo) {

        bindAvatarImage(mAvatarImage, acerInfo.avatar);
        mNameText.setText(acerInfo.name);
        mDescriptionText.setText(acerInfo.sign);

        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAccountTransitionRunning) {
                    return;
                }
                if (mListener != null) {
                    mListener.openProfile(null);
                }
            }
        });
    }

    public void bind() {

        if (mAdapter == null) {
            return;
        }

        bindActiveUser();
//        bindRecentUsers();
    }

    private void bindActiveUser() {

//        Context context = getContext();
//        mActiveAccount = AccountUtils.getActiveAccount(context);

//        UserInfo userInfo = mAdapter.getUserInfo(mActiveAccount);
//        if (userInfo != null) {
//            bindAvatarImage(mAvatarImage, userInfo.getLargeAvatarOrAvatar());
//            mNameText.setText(userInfo.name);
//            if (!TextUtils.isEmpty(userInfo.signature)) {
//                mDescriptionText.setText(userInfo.signature);
//            } else {
//                //noinspection deprecation
//                mDescriptionText.setText(userInfo.uid);
//            }
//        } else {
//            User partialUser = mAdapter.getPartialUser(mActiveAccount);
//            bindAvatarImage(mAvatarImage, null);
//            mNameText.setText(partialUser.name);
//            //noinspection deprecation
//            mDescriptionText.setText(partialUser.uid);
//        }
//        mAvatarImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mAccountTransitionRunning) {
//                    return;
//                }
//                if (mListener != null) {
//                    mListener.openProfile(mActiveAccount);
//                }
//            }
//        });

        //mBackdropImage.setImageResource();

        bindAvatarImage(mAvatarImage, " ");
        mNameText.setText("name");
        mDescriptionText.setText("description");

        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAccountTransitionRunning) {
                    return;
                }
                if (mListener != null) {
                    mListener.openProfile(null);
                }
            }
        });
    }

    private void bindRecentUsers() {
        Context context = getContext();
//        mRecentOneAccount = AccountUtils.getRecentOneAccount(context);
//        bindRecentUser(mRecentOneAvatarImage, mRecentOneAccount);
//        mRecentTwoAccount = AccountUtils.getRecentTwoAccount(context);
//        bindRecentUser(mRecentTwoAvatarImage, mRecentTwoAccount);
    }

    private void bindRecentUser(ImageView avatarImage, final Account account) {

        if (account == null) {
            avatarImage.setVisibility(GONE);
            return;
        }

        UserInfo userInfo = mAdapter.getUserInfo(account);
        if (userInfo != null) {
            bindAvatarImage(avatarImage, userInfo.getLargeAvatarOrAvatar());
        } else {
            bindAvatarImage(avatarImage, null);
        }
        avatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToAccountWithTransitionIfNotRunning(account);
            }
        });
    }

    private void bindAvatarImage(ImageView avatarImage, String avatarUrl) {

        if (TextUtils.isEmpty(avatarUrl)) {
            avatarImage.setImageResource(mika.com.android.ac.R.drawable.avatar_icon_white_inactive_64dp);
            avatarImage.setTag(null);
            return;
        }

        for (ImageView anotherAvatarImage : mAvatarImages) {
            String anotherAvatarUrl = (String) anotherAvatarImage.getTag();
            if (TextUtils.equals(anotherAvatarUrl , avatarUrl)) {
                setAvatarImageFrom(avatarImage, anotherAvatarImage);
                return;
            }
        }

        ImageUtils.loadNavigationHeaderAvatar(avatarImage, avatarUrl);
    }

    private void setAvatarImageFrom(ImageView toAvatarImage, ImageView fromAvatarImage) {
        if (toAvatarImage == fromAvatarImage) {
            return;
        }
        toAvatarImage.setImageDrawable(fromAvatarImage.getDrawable());
        toAvatarImage.setTag(fromAvatarImage.getTag());
    }

    public void switchToAccountWithTransitionIfNotRunning(Account account) {

        if (mAccountTransitionRunning) {
            return;
        }

        showingAccountList(false);

        Context context = getContext();
//        if (AccountUtils.isActiveAccount(account, context)) {
//            return;
//        }

//        AccountUtils.setActiveAccount(account, context);
//        if (account.equals(mRecentOneAccount)) {
//            beginAvatarTransitionFromRecent(mRecentOneAvatarImage);
//        } else if (account.equals(mRecentTwoAccount)) {
//            beginAvatarTransitionFromRecent(mRecentTwoAvatarImage);
//        } else {
            beginAvatarTransitionFromNonRecent();
//        }
        bind();

        // TODO: Move to AccountUtils with ActiveAccountChangedEvent.
        if (mListener != null) {
            mListener.onActiveAccountChanged(account);
        }
    }

    private void beginAvatarTransitionFromRecent(ImageView recentAvatarImage) {
        beginAccountTransition(recentAvatarImage, mAvatarImage, null);
    }

    private void beginAvatarTransitionFromNonRecent() {
//        boolean hasRecentTwoAccount = mRecentTwoAccount != null;
//        beginAccountTransition(mAvatarImage, mRecentOneAvatarImage,
//                hasRecentTwoAccount ? mRecentTwoAvatarImage : null);

        beginAccountTransition(mAvatarImage, mRecentOneAvatarImage, null);
    }

    private void beginAccountTransition(ImageView moveAvatarOneImage,
                                        ImageView moveAvatarTwoImage,
                                        ImageView moveAvatarThreeImage) {

        ImageView appearAvatarImage = moveAvatarOneImage;
        ImageView disappearAvatarImage = moveAvatarThreeImage != null ? moveAvatarThreeImage
                : moveAvatarTwoImage;
        ImageView fadeOutDisappearAvatarImage =
                disappearAvatarImage == mAvatarImage ? mFadeOutAvatarImage
                        : disappearAvatarImage == mRecentOneAvatarImage ?
                        mFadeOutRecentOneAvatarImage : mFadeOutRecentTwoAvatarImage;

        TransitionSet transitionSet = new TransitionSet();
        int duration = ViewUtils.getLongAnimTime(getContext());
        // Will be set on already added and newly added transitions.
        transitionSet.setDuration(duration);
        // NOTE: TransitionSet.setInterpolator() won't have any effect on platform versions.
        // https://code.google.com/p/android/issues/detail?id=195495
        transitionSet.setInterpolator(new FastOutSlowInInterpolator());

        Fade fadeOutAvatar = new Fade(Fade.OUT);
        setAvatarImageFrom(fadeOutDisappearAvatarImage, disappearAvatarImage);
        fadeOutDisappearAvatarImage.setVisibility(VISIBLE);
        fadeOutAvatar.addTarget(fadeOutDisappearAvatarImage);
        transitionSet.addTransition(fadeOutAvatar);
        // Make it finish before new avatar arrives.
        fadeOutAvatar.setDuration(duration / 2);

        Fade fadeInAvatar = new Fade(Fade.IN);
        appearAvatarImage.setVisibility(INVISIBLE);
        fadeInAvatar.addTarget(appearAvatarImage);
        transitionSet.addTransition(fadeInAvatar);

        ChangeTransform changeAppearAvatarTransform = new ChangeTransform();
        appearAvatarImage.setScaleX(0.8f);
        appearAvatarImage.setScaleY(0.8f);
        changeAppearAvatarTransform.addTarget(appearAvatarImage);
        transitionSet.addTransition(changeAppearAvatarTransform);

        addChangeMoveToAvatarTransformToTransitionSet(moveAvatarOneImage, moveAvatarTwoImage,
                transitionSet);

        if (moveAvatarThreeImage != null) {
            addChangeMoveToAvatarTransformToTransitionSet(moveAvatarTwoImage, moveAvatarThreeImage,
                    transitionSet);
        }

        CrossfadeText crossfadeText = new CrossfadeText();
        crossfadeText.addTarget(mNameText);
        crossfadeText.addTarget(mDescriptionText);
        transitionSet.addTransition(crossfadeText);

        transitionSet.addListener(new Transition.TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAccountTransitionRunning = false;
                mInfoLayout.setEnabled(true);
            }
        });
        mInfoLayout.setEnabled(false);
        TransitionManager.beginDelayedTransition(this, transitionSet);
        mAccountTransitionRunning = true;

        fadeOutDisappearAvatarImage.setVisibility(INVISIBLE);

        appearAvatarImage.setVisibility(VISIBLE);
        appearAvatarImage.setScaleX(1);
        appearAvatarImage.setScaleY(1);

        resetMoveToAvatarTransform(moveAvatarTwoImage);
        if (moveAvatarThreeImage != null) {
            resetMoveToAvatarTransform(moveAvatarThreeImage);
        }
    }

    private void addChangeMoveToAvatarTransformToTransitionSet(ImageView moveFromAvatarImage,
                                                               ImageView moveToAvatarImage,
                                                               TransitionSet transitionSet) {
        ChangeTransform changeMoveToAvatarTransform = new ChangeTransform();
        moveToAvatarImage.setX(moveFromAvatarImage.getLeft()
                + (moveFromAvatarImage.getWidth() - moveToAvatarImage.getWidth()) / 2);
        moveToAvatarImage.setY(moveFromAvatarImage.getTop()
                + (moveFromAvatarImage.getHeight() - moveToAvatarImage.getHeight()) / 2);
        moveToAvatarImage.setScaleX((float) ViewUtils.getWidthExcludingPadding(moveFromAvatarImage)
                / ViewUtils.getWidthExcludingPadding(moveToAvatarImage));
        moveToAvatarImage.setScaleY((float) ViewUtils.getHeightExcludingPadding(moveFromAvatarImage)
                / ViewUtils.getHeightExcludingPadding(moveToAvatarImage));
        changeMoveToAvatarTransform.addTarget(moveToAvatarImage);
        transitionSet.addTransition(changeMoveToAvatarTransform);
    }

    private void resetMoveToAvatarTransform(ImageView moveToAvatarImage) {
        moveToAvatarImage.setTranslationX(0);
        moveToAvatarImage.setTranslationY(0);
        moveToAvatarImage.setScaleX(1);
        moveToAvatarImage.setScaleY(1);
    }

    private void showingAccountList(boolean show) {

        if (mShowingAccountList == show) {
            return;
        }

        if (mListener == null) {
            return;
        }

        mDropDownImage.animate()
                .rotation(show ? 180 : 0)
                .setDuration(ViewUtils.getShortAnimTime(getContext()))
                .start();
        mListener.showAccountList(show);
        mShowingAccountList = show;
    }

    public interface Adapter {
        User getPartialUser(Account account);
        UserInfo getUserInfo(Account account);
        Acer getAcer();
    }

    public interface Listener {
        void openProfile(Account account);
        void showAccountList(boolean show);
        void onActiveAccountChanged(Account newAccount);
    }
}
