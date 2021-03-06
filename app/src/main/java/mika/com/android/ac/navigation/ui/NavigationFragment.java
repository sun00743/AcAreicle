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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.account.content.AccountUserInfoResource;
import mika.com.android.ac.account.info.AccountContract;
import mika.com.android.ac.account.ui.AcerSignInActivity;
import mika.com.android.ac.db.AcerDB;
import mika.com.android.ac.link.NotImplementedManager;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.LoginRequest;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.acapi.AcerInfoResult2;
import mika.com.android.ac.network.api.info.acapi.LoginResult;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.profile.ui.ProfileActivity;
import mika.com.android.ac.quote.ui.QuoteActivity;
import mika.com.android.ac.settings.info.Settings;
import mika.com.android.ac.settings.ui.SettingsActivity;
import mika.com.android.ac.ui.DrawerManager;
import mika.com.android.ac.util.AppContact;
import mika.com.android.ac.util.GsonHelper;
import mika.com.android.ac.util.SharedPrefsUtils;

import static android.app.Activity.RESULT_OK;

public class NavigationFragment extends Fragment implements
        AccountUserInfoResource.Listener,
        NavigationHeaderLayout.Adapter,
        NavigationHeaderLayout.Listener,
        NavigationAccountListLayout.Adapter,
        NavigationAccountListLayout.Listener {

    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    private NavigationHeaderLayout mHeaderLayout;
    private TextView quoteCountText;
    private NavigationViewAdapter mNavigationViewAdapter;
    private OnNavigationMenuClickListener mMenuClickListener;
    private Acer mAcer;
    private AcerInfo2 acerInfo;
    private IsSigninEntry entry;

    public static NavigationFragment newInstance() {
        //noinspection deprecation
        return new NavigationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationMenuClickListener) {
            mMenuClickListener = (OnNavigationMenuClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement onNavigationMenuClickListener");
        }
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
        //get textView from actionLayout
        View actionView = mNavigationView.getMenu().getItem(2).getActionView();
        quoteCountText = ButterKnife.findById(actionView, R.id.quote_count);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHeaderLayout.setIsSigninEntry(entry);
        mHeaderLayout.setAdapter(this);
        mHeaderLayout.setListener(this);

        mAcer = new AcerDB(getActivity().getApplicationContext()).getAcer();
        if (mAcer != null) {
            AcWenApplication.getInstance().setAcer(mAcer);
            AccountContract.setLogin(true);
            //get acerinfo
            loadAcerInfo();
            // auto login
//            autoLogin();
            // is signin ?
            checkSignIn();
        } else {
            AccountContract.setLogin(false);
            mHeaderLayout.bind();
        }

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                mMenuClickListener.onHomeClicked();
                                break;
                            case R.id.navigation_message:
                                mMenuClickListener.onMessageClicked();
                                break;
                            case R.id.navigation_quote:
//                                mMenuClickListener.onQuoteClicked();
                                openQuote();
                                setQuoteCount(0);
                                break;
                            case R.id.navigation_settings:
                                openSettings();
                                break;
                            case R.id.navigation_star:
                                openFavorite();
                                break;
                            default:
                                NotImplementedManager.showNotYetImplementedToast(getActivity());
                        }

                        closeDrawer();
                        return true;
                    }
                });

        mNavigationView.getMenu().getItem(0).setChecked(true);
        mNavigationViewAdapter = NavigationViewAdapter.override(mNavigationView, this, this);
    }

    /**
     * load acerInfo and banana;
     */
    private void loadAcerInfo() {
        Volley.getInstance().addToRequestQueue(ApiRequests.newAcerInfo().setListener(
                new Response.Listener<AcerInfoResult2>() {
                    @Override
                    public void onResponse(AcerInfoResult2 response) {
                        if (response.success) {
                            acerInfo = response.userjson;
                            //get banana
                            mHeaderLayout.getBanana();
                        } else {
                            acerInfo = null;
                        }
                        mHeaderLayout.bindAcer(acerInfo);
                    }
                }
        ).setErrorListener(
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("loadAcerInfo", "onErrorResponse: " + error.getMessage());
                    }
                }
        ));
    }

    private void autoLogin() {
        String id = Settings.ID.getValue(getActivity().getApplicationContext());
        String pwd = Settings.PWD.getValue(getActivity().getApplicationContext());

        if (!id.isEmpty() && !pwd.isEmpty()){
            LoginRequest mLoginRequest = new LoginRequest(id, pwd);
            mLoginRequest.setShouldCache(true);
            mLoginRequest.setListener(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    LoginResult mLoginResult = GsonHelper.get().fromJson((String) response, LoginResult.class);
                    if (mLoginResult.success) {
                        mAcer = mLoginResult.data;
                        new AcerDB(getActivity().getApplicationContext()).saveAcer(mAcer);
                        AcWenApplication.getInstance().setAcer(mAcer);
                        AccountContract.setLogin(true);
                        //get acerinfo
                        loadAcerInfo();
                    }
                }
            });
            //start request
            Volley.getInstance().addToRequestQueue(mLoginRequest);
        }
    }

    private void checkSignIn() {
        if (SharedPrefsUtils.getBoolean(entry, getContext())) {
            mHeaderLayout.mSignin.setText(R.string.navigation_head_has_signin);
            mHeaderLayout.mSignin.setClickable(false);
        }
    }

    public void setQuoteCount(int count) {
        if (count == 0)
            quoteCountText.setText(getString(R.string.empty));
        else {
            quoteCountText.setText(String.format(Locale.CHINESE, "（%1$d条新召唤）", count));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMenuClickListener = null;
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
        return new User();
    }

    @Override
    public UserInfo getUserInfo(Account account) {
        return new UserInfo();
    }

    @Override
    public Acer getAcer() {
        return mAcer == null ? new Acer() : mAcer;
    }

    /**
     * 获取acer info 失败
     */
    @Override
    public void onGetAcerInfoError() {
        loadAcerInfo();
    }

    /**
     * get banana information error
     */
    @Override
    public void onGetBananaError() {

    }

    /**
     * 打开Acer信息界面，或者登陆
     */
    @Override
    public void openProfile(Account account) {
        if (AccountContract.isLogin()) {
            startActivity(ProfileActivity.makeIntent(mAcer, acerInfo, getActivity()));
        } else {
            startActivityForResult(new Intent(getActivity(), AcerSignInActivity.class), AppContact.RequestCode.REQUEST_CODE_SIGN_IN);
        }
    }

    private void openQuote() {
        if (AccountContract.isLogin()) {
            Intent intent = new Intent(getActivity(), QuoteActivity.class);
//        ActivityCompat.startActivity(this, intent, null);
            startActivityForResult(intent, AppContact.RequestCode.REQUEST_CODE_QUOTE);
        } else {
            startActivityForResult(new Intent(getActivity(), AcerSignInActivity.class), AppContact.RequestCode.REQUEST_CODE_SIGN_IN);
        }
    }


    /**
     * 登录activity的result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppContact.RequestCode.REQUEST_CODE_QUOTE:
                    acerInfo = data.getParcelableExtra(getString(R.string.acer_info));
                    mHeaderLayout.bindAcer(acerInfo);
                    mHeaderLayout.getBanana();
                    checkSignIn();
                    break;
                case AppContact.RequestCode.REQUEST_CODE_SIGN_IN:
                    acerInfo = data.getParcelableExtra(getString(R.string.acer_info));
                    mHeaderLayout.bindAcer(acerInfo);
                    mHeaderLayout.getBanana();
                    checkSignIn();
                    break;
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

    /**
     * 打开收藏界面
     */
    private void openFavorite() {
    }

    private void closeDrawer() {
        ((DrawerManager) getActivity()).closeDrawer(getView());
    }

    public interface OnNavigationMenuClickListener {
        void onHomeClicked();

        void onStarClicked();

        void onQuoteClicked();

        void onMessageClicked();
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
