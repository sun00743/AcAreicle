/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.articlelist.content;


import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.eventbus.ArticleDesDeletedEvent;
import mika.com.android.ac.eventbus.ArticleDesUpdatedEvent;
import mika.com.android.ac.eventbus.ArticleDesWriteFinishedEvent;
import mika.com.android.ac.eventbus.ArticleDesWriteStartedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.RequestFragment;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.acapi.ArticleList;
import mika.com.android.ac.network.api.info.acapi.ArticleListResult;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.app.TargetedRetainedFragment;

/**
 * 2016 09 05 ming kai<sun00743@gmail.com>
 * 文章综合列表 请求和回复 资源行为控制 fragment
 */
public class ArticleListResFragment extends ResourceFragment implements
        RequestFragment.Listener<ArticleListResult, ArticleListResFragment.State> {

    private static final String FRAGMENT_TAG_DEFAULT = ArticleListResFragment.class.getName();
    private static final int DEFAULT_COUNT_PER_LOAD = 20;

    protected static final String EXTRA_SORT = FRAGMENT_TAG_DEFAULT + "sort";
    protected static final String EXTRA_CHANNELID = FRAGMENT_TAG_DEFAULT + "channelid";

    private int pageNo;
    private int mSort;
    private int mChannelId;

    //    private ArticleListResult mArticleListResult;
    private List<ArticleList> mArticleLists;
    private boolean mCanLoadMore = true;
    /**
     * 是否正在loading
     */
    private boolean mLoading;
    /**
     * 是否加载更多
     */
    private boolean mLoadingMore;

    private static ArticleListResFragment newInstance(int channelid, int mSort) {
        ArticleListResFragment resource = new ArticleListResFragment();
        resource.setArguments(channelid, mSort);
        return resource;
    }

    public ArticleListResFragment() {
    }

    public static ArticleListResFragment attachTo(String userIdOrUid, int channelid, int mSort,
                                                  FragmentActivity activity, String tag,
                                                  int requestCode) {
        return attachTo(userIdOrUid, channelid, mSort, activity, tag, true, null, requestCode);
    }

    public static ArticleListResFragment attachTo(String userIdOrUid, int channelid, int mSort,
                                                  FragmentActivity activity) {
        return attachTo(userIdOrUid, channelid, mSort, activity, FRAGMENT_TAG_DEFAULT, TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    public static ArticleListResFragment attachTo(String userIdOrUid, int channelid, int mSort,
                                                  Fragment fragment, String tag, int requestCode) {
        return attachTo(userIdOrUid, channelid, mSort, fragment.getActivity(), tag, false, fragment,
                requestCode);
    }

    public static ArticleListResFragment attachTo(String userIdOrUid, int channelid, int mSort,
                                                  Fragment fragment) {
        return attachTo(userIdOrUid, channelid, mSort, fragment, FRAGMENT_TAG_DEFAULT, TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    private static ArticleListResFragment attachTo(String userIdOrUid, int channelid, int mSort,
                                                   FragmentActivity activity, String tag,
                                                   boolean targetAtActivity, Fragment targetFragment,
                                                   int requestCode) {
        ArticleListResFragment resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(channelid, mSort);
            if (targetAtActivity) {
                resource.targetAtActivity(requestCode);
            } else {
                resource.targetAtFragment(targetFragment, requestCode);
            }
            FragmentUtils.add(resource, activity, tag);
        }
        return resource;
    }

    /**
     * @return the fucking Unmodifiable articleLists or null
     */
    public List<ArticleList> get() {
        return mArticleLists != null ?
                Collections.unmodifiableList(mArticleLists) : null;
    }

    public boolean has() {
        return mArticleLists != null;
    }

    /**
     * 判断articleList是否为null
     *
     * @return true is empty, false not
     */
    public boolean isEmpty() {
        return mArticleLists == null ||
                mArticleLists.isEmpty();
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }


    protected void setArguments(int channelid, int sort) {
        Bundle arguments = FragmentUtils.ensureArguments(this);
//        arguments.putString(EXTRA_USER_ID_OR_UID, userIdOrUid);
        arguments.putInt(EXTRA_CHANNELID, channelid);
        arguments.putInt(EXTRA_SORT, sort);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
//        mUserIdOrUid = arguments.getString(EXTRA_USER_ID_OR_UID);
        mChannelId = arguments.getInt(EXTRA_CHANNELID);
        mSort = arguments.getInt(EXTRA_SORT);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.register(this);

        if (mArticleLists == null ||
                (mArticleLists.isEmpty() && mCanLoadMore)) {
            loadOnStart();
        }
    }

    protected void loadOnStart() {
        load(false);
    }

    /**
     * 加载数据
     *
     * @param loadMore 是否加载更多
     */
    public void load(boolean loadMore) {
        load(loadMore, DEFAULT_COUNT_PER_LOAD, mChannelId, mSort);
    }

    /**
     * 在此方法创建request并加载数据
     *
     * @param loadMore            是否加载更多
     * @param defaultCountPerLoad 加载count
     */
    public void load(boolean loadMore, int defaultCountPerLoad, int channelId, int sort) {
        if (mLoading || (loadMore && !mCanLoadMore)) {
            return;
        }

        mLoading = true;
        mLoadingMore = loadMore;
        getListStateListener().onLoadArticleListStarted(getRequestCode());

        onStartLoad();
        //文章ID
//        Long untilId = null;
//        if(loadMore && mArticleListResult != null){
//            int size = mArticleListResult.paramsData.articleLists.size();
//            if(size > 0){
//                untilId = mArticleListResult.paramsData.articleLists.get(size-1).id;
//            }
//        }

        if (!loadMore) pageNo = 1;
        ApiRequest<ArticleListResult> request =
                ApiRequests.newArticleListResultRequest(channelId, sort, pageNo);
        State state = new State(loadMore, defaultCountPerLoad);
        RequestFragment.startRequest(request, state, this);
    }

    protected void onStartLoad() {
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusUtils.unregister(this);
    }

    @Override
    public void onVolleyResponse(int requestCode, final boolean successful, final ArticleListResult result,
                                 final VolleyError error, final State requestState) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                OnLoadFinished(successful, result, error, requestState.loadMore, requestState.count);
            }
        });
    }

    private void OnLoadFinished(boolean successful, ArticleListResult result,
                                VolleyError error, boolean loadMore, int count) {
        //改变加载状态
        mLoading = false;
        mLoadingMore = false;
        //通知界面加载数据完成
        getListStateListener().onLoadArticleListFinished(getRequestCode());

        if (successful) {
            mCanLoadMore = result.paramsData.articleLists.size() == count;
            ++pageNo;
            if (loadMore) {
                mArticleLists.addAll(result.paramsData.articleLists);
                //通知界面loadMore完成
                getListStateListener().onArticleListAppended(getRequestCode(),
                        Collections.unmodifiableList(result.paramsData.articleLists));
                for (ArticleList articlelist : result.paramsData.articleLists) {
                    EventBusUtils.postAsync(new ArticleDesUpdatedEvent(articlelist, this));
                }
            } else {
                setArticleDesList(result.paramsData.articleLists);
            }
        } else {
            //返回错误信息
            getListStateListener().onLoadArticleListError(getRequestCode(), error);
        }
    }

    @Keep
    public void onEventMainThread(ArticleDesUpdatedEvent event) {
        if (event.isFromMyself(this) || mArticleLists == null) {
            return;
        }

        for (int i = 0, size = mArticleLists.size(); i < size; ++i) {
            ArticleList articleList = mArticleLists.get(i);
            boolean changed = false;
            if (articleList.id == event.articleDes.id) {
                mArticleLists.set(i, event.articleDes);
                changed = true;
            }
//            else if (articleList != null
//                    && articleList.id == event.articleDes.id) {
//                articleList = event.articleDes;
//                changed = true;
//            }
            if (changed) {
                getListStateListener().onArticleChanged(getRequestCode(), i,
                        mArticleLists.get(i));
            }
        }
    }

    @Keep
    public void onEventMainThread(ArticleDesDeletedEvent event) {
        if (event.isFromMyself(this) || mArticleLists == null) {
            return;
        }
    }

    @Keep
    public void onEventMainThread(ArticleDesWriteStartedEvent event) {
        if (event.isFromMyself(this) || mArticleLists == null) {
            return;
        }
    }

    @Keep
    public void onEventMainThread(ArticleDesWriteFinishedEvent event) {
        if (event.isFromMyself(this) || mArticleLists == null) {
            return;
        }
    }

    /**
     * 设置loading状态来通知视图更新加载数据状态
     */
    protected void setLoading(boolean loading) {
        if (mLoading == loading) {
            return;
        }
        mLoading = loading;
        if (mLoading) {
            getListStateListener().onLoadArticleListStarted(getRequestCode());
        } else {
            getListStateListener().onLoadArticleListFinished(getRequestCode());
        }
    }

    protected void setArticleDesList(ArrayList<ArticleList> articleLists) {
        this.mArticleLists = articleLists;
        //通知界面list数据修改
        getListStateListener().onArticleListChanged(getRequestCode(),
                Collections.unmodifiableList(articleLists));
    }

    public void setChannelId(int channelId) {
        mChannelId = channelId;
    }

    public void setSort(int sort) {
        mSort = sort;
    }

    private ListStateListener getListStateListener() {
        return (ListStateListener) getTarget();
    }

    static class State {

        public boolean loadMore;
        public int count;

        public State(boolean loadMore, int count) {
            this.loadMore = loadMore;
            this.count = count;
        }
    }

    /**
     * 加载数据状态和文章状态接口
     */
    public interface ListStateListener {
        /**
         * 回调通知 开始请求数据显示等待状态
         *
         * @param requestCode
         */
        void onLoadArticleListStarted(int requestCode);

        /**
         * 加载文章列表数据完成了，可以更新UI啦
         *
         * @param requestCode
         */
        void onLoadArticleListFinished(int requestCode);

        void onLoadArticleListError(int requestCode, VolleyError error);

        /**
         * @param newArticleList Unmodifiable.
         */
        void onArticleListChanged(int requestCode, List<ArticleList> newArticleList);

        /**
         * @param appendedArticleList Unmodifiable.
         */
        void onArticleListAppended(int requestCode, List<ArticleList> appendedArticleList);

        void onArticleChanged(int requestCode, int position, ArticleList newArticleDes);

        void onArticleRemoved(int requestCode, int position);

        void onArticleWriteStarted(int requestCode, int position);

        void onArticleWriteFinished(int requestCode, int position);
    }

}
