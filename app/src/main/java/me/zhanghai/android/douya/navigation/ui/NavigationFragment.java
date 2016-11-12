/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.navigation.ui;

import android.accounts.Account;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.account.content.AccountUserInfoResource;
import me.zhanghai.android.douya.account.ui.AcerSignInActivity;
import me.zhanghai.android.douya.db.AcerDB;
import me.zhanghai.android.douya.link.NotImplementedManager;
import me.zhanghai.android.douya.network.Volley;
import me.zhanghai.android.douya.network.api.AcerInfoRequest;
import me.zhanghai.android.douya.network.api.info.acapi.Acer;
import me.zhanghai.android.douya.network.api.info.acapi.AcerInfo2;
import me.zhanghai.android.douya.network.api.info.acapi.AcerInfoResult2;
import me.zhanghai.android.douya.network.api.info.apiv2.User;
import me.zhanghai.android.douya.network.api.info.apiv2.UserInfo;
import me.zhanghai.android.douya.profile.ui.ProfileActivity;
import me.zhanghai.android.douya.settings.ui.SettingsActivity;
import me.zhanghai.android.douya.ui.DrawerManager;

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

    public static NavigationFragment newInstance() {
        //noinspection deprecation
        return new NavigationFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public NavigationFragment() {}

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

        mHeaderLayout.setAdapter(this);
        mHeaderLayout.setListener(this);

        mAcer = new AcerDB(getActivity().getApplicationContext()).getAcer();
        if(mAcer != null){
            DouyaApplication.getInstance().setAcer(mAcer);
            DouyaApplication.LOGIN = true;
            //请求acerinfo
            AcerInfoRequest mAcerInfoRequest = new AcerInfoRequest(mAcer.userId);
            Volley.getInstance().addToRequestQueue(mAcerInfoRequest);

            mAcerInfoRequest.setListener(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    if(((AcerInfoResult2)response).success){
                        acerInfo = ((AcerInfoResult2)response).userjson;
                        mHeaderLayout.bindAcer(acerInfo);
                    }else{
                        //显示服务器错误信息
                    }
                }
            });
            mAcerInfoRequest.setErrorListener(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //显示请求错误
                }
            });

        }else{
            DouyaApplication.LOGIN = false;
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

    @Override
    public void onLoadUserInfoStarted(int requestCode) {}

    @Override
    public void onLoadUserInfoFinished(int requestCode) {}

    @Override
    public void onLoadUserInfoError(int requestCode, VolleyError error) {}

    @Override
    public void onUserInfoChanged(int requestCode, UserInfo newUserInfo) {
        mHeaderLayout.bind();
    }

    @Override
    public void onUserInfoWriteStarted(int requestCode) {}

    @Override
    public void onUserInfoWriteFinished(int requestCode) {}

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

        if(DouyaApplication.LOGIN){
            startActivity(ProfileActivity.makeIntent("" , getActivity()));
        }else{
            startActivityForResult(new Intent(getActivity(), AcerSignInActivity.class), DouyaApplication.REQUEST_CODE_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == DouyaApplication.REQUEST_CODE_SIGN_IN){
                acerInfo = data.getParcelableExtra("acer_info");
                mHeaderLayout.bindAcer(acerInfo);
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
}
