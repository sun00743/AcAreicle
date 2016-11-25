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

    @BindView(mika.com.android.ac.R.id.login_form)
    View mFormLayout;
    @BindView(mika.com.android.ac.R.id.login_username_layout)
    TextInputLayout mUsernameLayout;
    @BindView(mika.com.android.ac.R.id.login_username)
    EditText mUsernameEdit;
    @BindView(mika.com.android.ac.R.id.login_password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(mika.com.android.ac.R.id.login_password)
    EditText mPasswordEdit;
    @BindView(mika.com.android.ac.R.id.acer_login)
    Button mLoginButton;
    @BindView(mika.com.android.ac.R.id.login_progress)
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
        setContentView(mika.com.android.ac.R.layout.activity_acer_sign_in);
        ButterKnife.bind(this);

        mLoginRequest = new LoginRequest("月与萧","136892");
        mLoginRequest.setShouldCache(true);

        initListener();
    }

    private void initListener(){
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptStartAuth();
            }
        });

        mLoginRequest.setListener(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                mLoginResult = GsonHelper.get().fromJson((String) response, LoginResult.class);
                if(mLoginResult.success){
                    acer = mLoginResult.data;
                    new AcerDB(getApplicationContext()).saveAcer(acer);
                    AcWenApplication.getInstance().setAcer(acer);
                    AcWenApplication.LOGIN = true;
                    //请求acer详细信息
                    requestAcerInfo(acer.userId);
                }else{
                    //showError;
                }
            }
        });
        mLoginRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginError ", error.getMessage());
            }
        });
    }

    private void attemptStartAuth() {

        // Store values at the time of login attempt.
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();

        boolean cancel = false;
        View errorView = null;

        if (TextUtils.isEmpty(mUsername)) {
            mUsernameLayout.setError(getString(mika.com.android.ac.R.string.auth_error_empty_username));
            errorView = mUsernameEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordLayout.setError(getString(mika.com.android.ac.R.string.auth_error_empty_password));
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

    private void onStartAuth() {
        //view设置切换动画
        ViewUtils.crossfade(mFormLayout, mProgress, false);

        Volley.getInstance().addToRequestQueue(mLoginRequest);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ViewUtils.crossfade(mProgress, mFormLayout);
//            }
//        },2000);
    }

    private void requestAcerInfo(int userId) {
        mAcerInfoRequest = new AcerInfoRequest(userId);
        Volley.getInstance().addToRequestQueue(mAcerInfoRequest);

        mAcerInfoRequest.setListener(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if(((AcerInfoResult2)response).success){
                    acerInfo = ((AcerInfoResult2)response).userjson;
                    doResult();
                }else{
                    //显示服务器错误信息
                }
            }
        });
        mAcerInfoRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void doResult() {
        Intent result = new Intent();
        result.putExtra("acer_info",acerInfo);
        setResult(RESULT_OK,result);
        finish();
    }

}
