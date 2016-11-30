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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.account.content.AccountUserInfoResource;
import mika.com.android.ac.account.ui.AcerSignInActivity;
import mika.com.android.ac.db.AcerDB;
import mika.com.android.ac.link.NotImplementedManager;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.AcerInfoRequest;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.acapi.AcerInfoResult2;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.profile.ui.ProfileActivity;
import mika.com.android.ac.settings.ui.SettingsActivity;
import mika.com.android.ac.ui.DrawerManager;
import mika.com.android.ac.util.SharedPrefsUtils;

import static android.app.Activity.RESULT_OK;

public class NavigationFragment extends Fragment implements AccountUserInfoResource.Listener,
        NavigationHeaderLayout.Adapter, NavigationHeaderLayout.Listener,
        NavigationAccountListLayout.Adapter, NavigationAccountListLayout.Listener {

    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    private NavigationHeaderLayout mHeaderLayout;

//    private ArrayMap<Account, AccountUserInfoResource> mUserInfoResourceMap;

    private NavigationViewAdapter mNavigationViewAdapter;

    private Acer mAcer;
    private AcerInfo2 acerInfo;
    private IsSigninEntry entry;

    public static NavigationFragment newInstance() {
        //noinspection deprecation
        return new NavigationFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public NavigationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        entry = new IsSigninEntry();
        mHeaderLayout = (NavigationHeaderLayout) mNavigationView.getHeaderView(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Activity activity = getActivity();
//        mUserInfoResourceMap = new ArrayMap<>();
//        for (Account account : AccountUtils.getAccounts(activity)) {
//            mUserInfoResourceMap.put(account, AccountUserInfoResource.attachTo(account, this,
//                    account.name, -1));
//        }

        mHeaderLayout.setIsSigninEntry(entry);
        mHeaderLayout.setAdapter(this);
        mHeaderLayout.setListener(this);

        mAcer = new AcerDB(getActivity().getApplicationContext()).getAcer();
        if (mAcer != null) {
            AcWenApplication.getInstance().setAcer(mAcer);
            AcWenApplication.LOGIN = true;
            //get acerinfo
            AcerInfoRequest mAcerInfoRequest = new AcerInfoRequest(mAcer.userId);
            Volley.getInstance().addToRequestQueue(mAcerInfoRequest);
            mAcerInfoRequest.setListener(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    if (((AcerInfoResult2) response).success) {

                        acerInfo = ((AcerInfoResult2) response).userjson;
                        mHeaderLayout.bindAcer(acerInfo);

                        //get banana
                        mHeaderLayout.getBanana();
                    } else {
                        //todo 显示服务器错误信息
                    }
                }
            }).setErrorListener(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // is signin ?
            checkSignIn();

        } else {
            AcWenApplication.LOGIN = false;
            mHeaderLayout.bind();
        }

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:

                                break;
                            case R.id.navigation_settings:
                                openSettings();
                                break;
                            case R.id.navigation_star:
                                //todo 收藏界面
                                break;
                            default:

                                NotImplementedManager.showNotYetImplementedToast(getActivity());
                        }

                        if (menuItem.getGroupId() == R.id.navigation_group_primary) {
                            menuItem.setChecked(true);
                        }
                        closeDrawer();
                        return true;
                    }
                });

        mNavigationView.getMenu().getItem(0).setChecked(true);
        mNavigationViewAdapter = NavigationViewAdapter.override(mNavigationView, this, this);
    }

    private void checkSignIn() {
        if (SharedPrefsUtils.getBoolean(entry, getContext())) {
            mHeaderLayout.mSignin.setText(R.string.navigation_head_has_signin);
            mHeaderLayout.mSignin.setClickable(false);
        }
    }

    @Override
    public void onLoadUserInfoStarted(int requestCode) {
    }

    @Override
    public void onLoadUserInfoFinished(int requestCode) {
    }

    @Override
    public void onLoadUserInfoError(int requestCode, VolleyError error) {
    }

    @Override
    public void onUserInfoChanged(int requestCode, UserInfo newUserInfo) {
        mHeaderLayout.bind();
    }

    @Override
    public void onUserInfoWriteStarted(int requestCode) {
    }

    @Override
    public void onUserInfoWriteFinished(int requestCode) {
    }

    @Override
    public User getPartialUser(Account account) {
//        return mUserInfoResourceMap.get(account).getPartialUser();
        return new User();
    }

    @Override
    public UserInfo getUserInfo(Account account) {
//        return mUserInfoResourceMap.get(account).getUserInfo();
        return new UserInfo();
    }

    @Override
    public Acer getAcer() {
        return mAcer == null ? new Acer() : mAcer;
    }

    /**
     * 打开Acer信息界面，或者登陆
     */
    @Override
    public void openProfile(Account account) {
//        UserInfoResource userInfoResource = mUserInfoResourceMap.get(account);
//        Intent intent;
//        if (userInfoResource.hasUserInfo()) {
//            intent = ProfileActivity.makeIntent(userInfoResource.getUserInfo(), getActivity());
//        } else {
//            // If we don't have user info, then user must also be partial. In this case we
//            // can only pass user id or uid.
//            intent = ProfileActivity.makeIntent(userInfoResource.getUserIdOrUid(), getActivity());
//        }
//        startActivity(intent);

        if (AcWenApplication.LOGIN) {
            startActivity(ProfileActivity.makeIntent(mAcer, acerInfo, getActivity()));
        } else {
            startActivityForResult(new Intent(getActivity(), AcerSignInActivity.class), AcWenApplication.REQUEST_CODE_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AcWenApplication.REQUEST_CODE_SIGN_IN) {
                acerInfo = data.getParcelableExtra("acer_info");
                mHeaderLayout.bindAcer(acerInfo);
                mHeaderLayout.getBanana();
                checkSignIn();
            }
        }
    }

    @Override
    public void showAccountList(boolean show) {
        mNavigationViewAdapter.showAccountList(show);
    }

    @Override
    public void onActiveAccountChanged(Account newAccount) {
    }

    @Override
    public void switchToAccount(Account account) {
        mHeaderLayout.switchToAccountWithTransitionIfNotRunning(account);
    }

    /**
     * 打开设置界面
     */
    private void openSettings() {
        startActivity(SettingsActivity.makeIntent(getActivity()));
    }

    private void closeDrawer() {
        ((DrawerManager) getActivity()).closeDrawer(getView());
    }

    /**
     * 当天是否签到prefsEntry
     */
    public class IsSigninEntry implements SharedPrefsUtils.Entry<Boolean> {

        @Override
        public String getKey(Context context) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis());
            return "is_signin_key" + date;
        }

        @Override
        public Boolean getDefaultValue(Context context) {
            return false;
        }

    }
}
