package me.zhanghai.android.douya.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.article.ui.ArticleActivity2;
import me.zhanghai.android.douya.util.FileUtil;

public class TestActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, TestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        ApiRequest<ArticleListResult> request = ApiRequests.newArticleListResultRequest(0,0,0);
//        request.setListener(new Response.Listener<ArticleListResult>() {
//            @Override
//            public void onResponse(ArticleListResult response) {
//                Log.i("response" , response + "");
//                ArrayList<ArticleList> lists = response.paramsData.articleLists;
//                String name = lists.get(0).username;
//            }
//        });
//        Volley.getInstance().addToRequestQueue(request);
//        RequestFragment.startRequest(request,null,new FragmentActivity());

//        ArticleActivity.start(TestActivity.this,3089134,"itititit");
        startActivity(new Intent(TestActivity.this, ArticleActivity2.class));
    }

    @Override
    protected void onDestroy() {
        FileUtil.deleteFiles(getExternalCacheDir());
        super.onDestroy();
    }
}
