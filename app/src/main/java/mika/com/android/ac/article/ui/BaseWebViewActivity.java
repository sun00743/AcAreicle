/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.article.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.IOException;
import java.util.Locale;

import mika.com.android.ac.R;


/**
 * @author Yrom
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
@TargetApi(19)
public class BaseWebViewActivity extends AppCompatActivity {
    public static final String UA = "acfun/1.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+Build.MODEL+"; "+ Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry().toLowerCase()+") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";

    protected WebView mWeb;
    protected View mProgress;
    
    @Override
    protected void onPause() {
        super.onPause();
        mWeb.pauseTimers();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mWeb.resumeTimers();
    }
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        mProgress = findViewById(R.id.article_Progress);
        mWeb = (WebView) findViewById(R.id.article_webview);
        mWeb.getSettings().setAllowFileAccess(true);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setUserAgentString(UA);
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        /*
         * fixed issues #12
         * http://stackoverflow.com/questions/9476151/webview-flashing-with-white-background-if-hardware-acceleration-is-enabled-an
         */
        if(Build.VERSION.SDK_INT >= 11)
//            mWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        initView(savedInstanceState);
        initData();
    }

    protected DialogInterface.OnClickListener mErrorDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (which == DialogInterface.BUTTON_POSITIVE) {
                initData();
            } else {
                finish();
            }
        }
    };

    protected void showErrorDialog() {
        try {
            Drawable icon = Drawable.createFromStream(getAssets().open("emotion/ais/27.gif"), "27.gif");
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            new AlertDialog.Builder(this).setTitle("加载失败！").setIcon(icon).setMessage("是否重试？").setPositiveButton("重试", mErrorDialogListener)
                    .setNegativeButton("算了", mErrorDialogListener).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initView(Bundle savedInstanceState) {}

    protected void initData() {
        setSupportProgressBarIndeterminateVisibility(true);
    }
    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        mProgress.setVisibility(visible? View.VISIBLE: View.GONE);
        mWeb.setVisibility(visible? View.GONE: View.VISIBLE);
    }
    /**
     * 
     * @param script
     *            the JavaScript to execute.
     * @param resultCallback
     *            A callback to be invoked when the script execution completes
     *            with the result of the execution (if any). May be null if no
     *            notificaion of the result is required.
     */
    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                mWeb.evaluateJavascript(script, resultCallback);
                return;
            } catch (Exception ignored) {
            }
        }
        mWeb.loadUrl(script);
    }
}
