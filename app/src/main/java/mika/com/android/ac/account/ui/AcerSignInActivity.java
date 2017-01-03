/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.db.AcerDB;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.AcerInfoRequest;
import mika.com.android.ac.network.api.LoginRequest;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.AcerInfo2;
import mika.com.android.ac.network.api.info.acapi.AcerInfoResult2;
import mika.com.android.ac.network.api.info.acapi.LoginResult;
import mika.com.android.ac.util.GsonHelper;
import mika.com.android.ac.util.ViewUtils;

public class AcerSignInActivity extends AppCompatActivity {

    @BindView(R.id.login_form)
    View mFormLayout;
    @BindView(R.id.login_username_layout)
    TextInputLayout mUsernameLayout;
    @BindView(R.id.login_username)
    EditText mUsernameEdit;
    @BindView(R.id.login_password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.login_password)
    EditText mPasswordEdit;
    @BindView(R.id.acer_login)
    Button mLoginButton;
    @BindView(R.id.login_progress)
    ProgressBar mProgress;

    private String mUsername;
    private String mPassword;
    private LoginRequest mLoginRequest;
    private AcerInfoRequest mAcerInfoRequest;
    private LoginResult mLoginResult;
    private Acer acer;
    private AcerInfo2 acerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acer_sign_in);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptStartAuth();
            }
        });
    }

    /**
     * 检查用户名密码是否和法
     */
    private void attemptStartAuth() {

        // Store values at the time of login attempt.
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();

        boolean cancel = false;
        View errorView = null;

        if (TextUtils.isEmpty(mUsername)) {
            mUsernameLayout.setError(getString(R.string.auth_error_empty_username));
            errorView = mUsernameEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordLayout.setError(getString(R.string.auth_error_empty_password));
            if (errorView == null) {
                errorView = mPasswordEdit;
            }
            cancel = true;
        }

        if (cancel) {
            errorView.requestFocus();
        } else {
            onStartAuth();
        }
    }

    /**
     * 登录
     */
    private void onStartAuth() {
        //view设置切换动画
        ViewUtils.crossfade(mFormLayout, mProgress, false);

        mLoginRequest = new LoginRequest(mUsername, mPassword);
        mLoginRequest.setShouldCache(true);
        mLoginRequest.setListener(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                mLoginResult = GsonHelper.get().fromJson((String) response, LoginResult.class);
                if (mLoginResult.success) {
                    acer = mLoginResult.data;
                    new AcerDB(getApplicationContext()).saveAcer(acer);
                    AcWenApplication.getInstance().setAcer(acer);
                    AcWenApplication.LOGIN = true;
                    //请求acer详细信息
                    requestAcerInfo(acer.userId);
                } else {
                    ViewUtils.crossfade(mProgress, mFormLayout);
                    if (mLoginResult.info.contains(getResources().getString(R.string.login_error_password))) {
                        mPasswordLayout.setError(mLoginResult.info);
                    } else {
                        mUsernameLayout.setError(mLoginResult.info);
                    }
                }
            }
        });
        mLoginRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginError ", " " + error.getMessage());
                ViewUtils.crossfade(mProgress, mFormLayout);

                Snackbar.make(getWindow().getDecorView(), R.string.login_error_timeout, Snackbar.LENGTH_LONG).show();
            }
        });
        //start request
        Volley.getInstance().addToRequestQueue(mLoginRequest);
    }

    private void requestAcerInfo(int userId) {
        mAcerInfoRequest = new AcerInfoRequest(userId);

        Volley.getInstance().addToRequestQueue(mAcerInfoRequest);
        mAcerInfoRequest.setListener(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if (((AcerInfoResult2) response).success) {
                    acerInfo = ((AcerInfoResult2) response).userjson;
                } else {
                    acerInfo = null;
                }
                doResult(acerInfo);
            }
        });
        mAcerInfoRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getAcerInfo", "onErrorResponse: " + error.getMessage());
                acerInfo = null;
                doResult(acerInfo);
            }
        });
    }

    private void doResult(AcerInfo2 acerInfo) {
        Intent result = new Intent();
        if (acerInfo != null) {
            result.putExtra("acer_info", acerInfo);
        }
        setResult(RESULT_OK, result);
        finish();
    }

}
