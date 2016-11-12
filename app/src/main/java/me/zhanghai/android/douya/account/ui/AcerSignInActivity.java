package me.zhanghai.android.douya.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.db.AcerDB;
import me.zhanghai.android.douya.network.Volley;
import me.zhanghai.android.douya.network.api.AcerInfoRequest;
import me.zhanghai.android.douya.network.api.LoginRequest;
import me.zhanghai.android.douya.network.api.info.acapi.Acer;
import me.zhanghai.android.douya.network.api.info.acapi.AcerInfo2;
import me.zhanghai.android.douya.network.api.info.acapi.AcerInfoResult2;
import me.zhanghai.android.douya.network.api.info.acapi.LoginResult;
import me.zhanghai.android.douya.util.GsonHelper;
import me.zhanghai.android.douya.util.ViewUtils;

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
                    DouyaApplication.getInstance().setAcer(acer);
                    DouyaApplication.LOGIN = true;
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
