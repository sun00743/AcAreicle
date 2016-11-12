package me.zhanghai.android.douya.articlelist.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.douya.account.util.AccountUtils;
import me.zhanghai.android.douya.network.api.info.acapi.ArticleList;
import me.zhanghai.android.douya.settings.info.Settings;
import me.zhanghai.android.douya.util.Callback;
import me.zhanghai.android.douya.util.FragmentUtils;

/**
 * Created by Administrator on 2016/9/7.
 */

public class HomeArticleListResFragment extends ArticleListResFragment {

    private static final String FRAGMENT_TAG_DEFAULT = HomeArticleListResFragment.class.getName();

    private final Handler mHandler = new Handler();

    private Account account;
    private int mChannelId;
    private int mSort;

    /**
     * 在Fragment stop的时候控制不要从磁盘中读取缓存
     */
    private boolean mStopped;

    private static HomeArticleListResFragment newInstance(int channelId, int sort){
        HomeArticleListResFragment resFragment = new HomeArticleListResFragment();
        resFragment.setArguments(null,channelId,sort);
        return resFragment;
    }

    public HomeArticleListResFragment() {}

    public static HomeArticleListResFragment attachTo(FragmentActivity activity,
                                                      int channelId, int sort){
        return attachTo(activity,channelId,sort,FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static HomeArticleListResFragment attachTo(FragmentActivity activity,
                                                      int channelId, int sort, String tag,
                                                      int requestCode){
        return attachTo(activity,channelId,sort, tag, true, null, requestCode);
    }

    public static HomeArticleListResFragment attachTo(Fragment fragment ,
                                                      int channelId, int sort){
        return attachTo(fragment,channelId,sort, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static HomeArticleListResFragment attachTo(Fragment fragment,
                                                      int channelId, int sort,String tag,
                                                      int requestCode){
        return attachTo(fragment.getActivity(),channelId,sort, tag, false, fragment, requestCode);
    }
    /**
     * 交付到FragmentUtils中管理（在FragmentUtils中会获取相应的fragmentManager并调用beginTransaction）
     * @param activity
     * @param tag
     * @param targetActivity
     * @param targetFragment
     * @param requestCode
     * @return
     */
    private static HomeArticleListResFragment attachTo(FragmentActivity activity ,
                                                       int channelId, int sort, String tag,
                                                       boolean targetActivity, Fragment targetFragment,
                                                       int requestCode){
        HomeArticleListResFragment resFragment = FragmentUtils.findByTag(activity,tag);
        if(resFragment == null){
            resFragment = newInstance(channelId,sort);
            if(targetActivity){
                resFragment.targetAtActivity(requestCode);
            }else{
                resFragment.targetAtFragment(targetFragment, requestCode);
            }
            FragmentUtils.add(resFragment, activity, tag);
        }
        return resFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannelId = getArguments().getInt(EXTRA_CHANNELID);
        mSort = getArguments().getInt(EXTRA_SORT);
    }

    @Override
    public void onStart() {
        super.onStart();
        //此方法会调用loadOnStart()
        mStopped = false;
    }

    @Override
    protected void loadOnStart() {
        loadFromCache();
    }

    private void loadFromCache() {
        if(isLoading()){
            return;
        }

        setLoading(true);
        onStartLoad();
        HomeArticleDesListCache.get(account, mChannelId, mHandler, new Callback<List<ArticleList>>() {
            @Override
            public void onValue(List<ArticleList> articleLists) {
                onLoadFromCacheComplete(articleLists);
            }
        }, getActivity());
    }

    private void onLoadFromCacheComplete(List<ArticleList> articleLists) {
        setLoading(false);
        if(mStopped){
            return;
        }
        boolean hasCache = articleLists != null && articleLists.size() > 0;
        if (hasCache) {
            setArticleDesList((ArrayList<ArticleList>) articleLists);
        }

        if (!hasCache || Settings.AUTO_REFRESH_HOME.getValue(getActivity())) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mStopped) {
                        return;
                    }
                    load(false);
                }
            });
        }
    }

    /**
     * 初始化用户
     */
    @Override
    protected void onStartLoad() {
        super.onStartLoad();

        account = AccountUtils.getActiveAccount(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();

        mStopped = true;
        List<ArticleList> articleLists = get();
        if (articleLists != null && articleLists.size() > 0) {
            saveToCache(articleLists);
        }
    }

    private void saveToCache(List<ArticleList> articleLists) {
        HomeArticleDesListCache.put(account, mChannelId, articleLists, getActivity());
    }
}
