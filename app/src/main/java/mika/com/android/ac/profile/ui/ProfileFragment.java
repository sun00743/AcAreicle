package mika.com.android.ac.profile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.followship.content.FollowUserManager;
import mika.com.android.ac.link.NotImplementedManager;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.network.api.info.frodo.Diary;
import mika.com.android.ac.network.api.info.frodo.Review;
import mika.com.android.ac.network.api.info.frodo.UserItems;
import mika.com.android.ac.profile.content.ProfileResource;
import mika.com.android.ac.profile.util.ProfileUtils;
import mika.com.android.ac.ui.ContentStateLayout;
import mika.com.android.ac.ui.CopyTextDialogFragment;
import mika.com.android.ac.ui.FlexibleSpaceContentScrollView;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;

public class ProfileFragment extends Fragment implements ProfileResource.Listener,
        ProfileHeaderLayout.Listener,
        ProfileIntroductionLayout.Listener,
        ConfirmUnfollowUserDialogFragment.Listener {

    private static final String KEY_PREFIX = ProfileFragment.class.getName() + '.';

    private static final String EXTRA_ACER = KEY_PREFIX + "acer";
    private static final String EXTRA_ACER_INFO = KEY_PREFIX + "acer_info";

    @BindView(R.id.scroll)
    ProfileLayout mScrollLayout;
    @BindView(R.id.header)
    ProfileHeaderLayout mHeaderLayout;
    @BindView(R.id.dismiss)
    View mDismissView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentState)
    ContentStateLayout mContentStateLayout;
    @BindView(R.id.content)
    FlexibleSpaceContentScrollView mContentView;
    @BindView(R.id.profile_uid)
    TextView mUid;
    @BindView(R.id.profile_regdate)
    TextView mRegDate;
    @BindView(R.id.profile_exp_progress)
    ProgressBar mExpProgress;
    @BindView(R.id.profile_exp_description)
    TextView mExpDescription;

    private Acer mAcer;
    private AcerInfo2 mAcerInfo;

    public static ProfileFragment newInstance(Acer acer, AcerInfo2 acerInfo2) {
        //noinspection deprecation
        ProfileFragment fragment = new ProfileFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putParcelable(EXTRA_ACER, acer);
        arguments.putParcelable(EXTRA_ACER_INFO, acerInfo2);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mAcer = arguments.getParcelable(EXTRA_ACER);
        mAcerInfo = arguments.getParcelable(EXTRA_ACER_INFO);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int layoutRes = ProfileUtils.shouldUseWideLayout(inflater.getContext()) ?
                R.layout.profile_fragment_wide : R.layout.profile_fragment;
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        CustomTabsHelperFragment.attachTo(this);
//        mProfileResource = ProfileResource.attachTo(mUserIdOrUid, new User(), new UserInfo(), this);

        mScrollLayout.setListener(new ProfileLayout.Listener() {
            @Override
            public void onEnterAnimationEnd() {

            }

            @Override
            public void onExitAnimationEnd() {
                getActivity().finish();
            }
        });
        if (savedInstanceState == null) {
            mScrollLayout.enter();
        }

        mDismissView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle(null);

        mHeaderLayout.bindAcerInfo(mAcerInfo);
        initContent();
    }

    private void initContent() {
        mUid.setText(String.valueOf(mAcerInfo.uid));
        mRegDate.setText(mAcerInfo.regTime);
        mExpDescription.setText("Lv:" + mAcerInfo.level + " - Exp:" + mAcerInfo.currExp + "/" + mAcerInfo.nextLevelNeed
                + " - Per:" + mAcerInfo.expPercent);
        mExpProgress.setProgress(mAcerInfo.expPercent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mProfileResource.detach();
    }

    public void onBackPressed() {
        exit();
    }

    private void exit() {
        mScrollLayout.exit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // TODO: Block or unblock.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_doumail:
                // TODO
                NotImplementedManager.showNotYetImplementedToast(getActivity());
                return true;
            case R.id.action_blacklist:
                // TODO
                NotImplementedManager.showNotYetImplementedToast(getActivity());
                return true;
            case R.id.action_report_abuse:
                // TODO
                NotImplementedManager.showNotYetImplementedToast(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLoadError(int requestCode, VolleyError error) {
        LogUtils.e(error.toString());
        mContentStateLayout.setError();
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onUserInfoChanged(int requestCode, UserInfo newUserInfo) {
//        mHeaderLayout.bindUserInfo(newUserInfo);
    }

    @Override
    public void onUserInfoWriteStarted(int requestCode) {
//        mHeaderLayout.bindUserInfo(mProfileResource.getUserInfo());
    }

    @Override
    public void onUserInfoWriteFinished(int requestCode) {
//        mHeaderLayout.bindUserInfo(mProfileResource.getUserInfo());
    }

    @Override
    public void onChanged(int requestCode, UserInfo newUserInfo, List<Broadcast> newBroadcastList,
                          List<User> newFollowingList, List<Diary> newDiaryList,
                          List<UserItems> newUserItemList, List<Review> newReviewList) {
    }

    @Override
    public void onEditProfile(UserInfo userInfo) {
        NotImplementedManager.showNotYetImplementedToast(getActivity());
    }

    @Override
    public void onFollowUser(UserInfo userInfo, boolean follow) {
        if (follow) {
            FollowUserManager.getInstance().write(userInfo, true, getActivity());
        } else {
            ConfirmUnfollowUserDialogFragment.show(this);
        }
    }

    @Override
    public void unfollowUser() {
//        FollowUserManager.getInstance().write(mProfileResource.getUserInfo(), false, getActivity());
    }

    @Override
    public void onCopyText(String text) {
        CopyTextDialogFragment.show(text, this);
    }
}
