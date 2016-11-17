/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.article.content;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import mika.com.android.ac.content.ResourceFragment;
import mika.com.android.ac.eventbus.CommentUpdatedEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.RequestFragment;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.network.api.info.acapi.CommentPage;
import mika.com.android.ac.network.api.info.acapi.CommentResult;
import mika.com.android.ac.util.ArrayUtils;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.app.TargetedRetainedFragment;

/**
 * Created by mika <sun00743@gmail.com> on 2016/9/18.
 */

public class CommentListResource extends ResourceFragment implements
        RequestFragment.Listener<CommentResult, CommentListResource.State> {

    private static final String FRAGMENT_TAG_DEFAULT = CommentListResource.class.getName();
    private static final int DEFAULT_COUNT_PER_LOAD = 40;
    private static final String EXTRA_ARTICLE = FRAGMENT_TAG_DEFAULT + "articleId";

    //    private ArticleListResult mArticleListResult;
    private int pageNO;
    private ArrayList<Integer> mCommentIdList;
    private Map<String,Comment> mCommentMaps;
    private SparseArray<Comment> mCommentLists ;
    private boolean mCanLoadMore = true;
    private int mArticleId;
    /**
     * 是否正在loading
     */
    private boolean mLoading;
    /**
     * 是否加载更多
     */
    private boolean mLoadingMore;

    private static CommentListResource newInstance(String userIdOrUid,int article){
        CommentListResource resource = new CommentListResource();
        resource.setArgumentS(article);
        return resource;
    }

    public CommentListResource (){}

    public static CommentListResource attachTo(String userIdOrUid,int articleId,
                                                  FragmentActivity activity, String tag,
                                                  int requestCode) {
        return attachTo(userIdOrUid,articleId, activity, tag, true, null, requestCode);
    }

    public static CommentListResource attachTo(String userIdOrUid,int articleId,
                                                  FragmentActivity activity) {
        return attachTo(userIdOrUid,articleId, activity, FRAGMENT_TAG_DEFAULT, TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    public static CommentListResource attachTo(String userIdOrUid,int articleId,
                                                  Fragment fragment, String tag, int requestCode) {
        return attachTo(userIdOrUid,articleId, fragment.getActivity(), tag, false, fragment,
                requestCode);
    }

    public static CommentListResource attachTo(String userIdOrUid,int articleId,
                                                  Fragment fragment) {
        return attachTo(userIdOrUid,articleId, fragment, FRAGMENT_TAG_DEFAULT, TargetedRetainedFragment.REQUEST_CODE_INVALID);
    }

    private static CommentListResource attachTo(String userIdOrUid,int articleId,
                                                   FragmentActivity activity, String tag,
                                                   boolean targetAtActivity, Fragment targetFragment,
                                                   int requestCode) {
        CommentListResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance(userIdOrUid,articleId);
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
     * @return the fucking Unmodifiable list or null
     */
    public Map<String,Comment> get() {
        return mCommentMaps != null ?
                Collections.unmodifiableMap(mCommentMaps) : null;
    }

    /**
     * the fucking Unmodifiable SparseArray or null
     * @return
     */
    public SparseArray<Comment> getSparseArray(){
        if(mCommentLists!= null){
            final SparseArray<Comment> sa = mCommentLists;
            return sa;
        }else {
            return null;
        }
    }

    public boolean has() {
        return mCommentLists != null;
    }

    /**
     * 判断articleList是否为null
     * @return
     */
    public boolean isEmpty() {
//        return mCommentMaps == null ||
//                mCommentMaps.isEmpty();
        return mCommentLists == null || mCommentLists.size() == 0;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }


    private void setArgumentS(int articleId){
        Bundle argumentsBundle = FragmentUtils.ensureArguments(this);
        argumentsBundle.putInt(EXTRA_ARTICLE,articleId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticleId = getArguments().getInt(EXTRA_ARTICLE);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.register(this);

        if(mCommentLists == null || (mCommentLists.size() == 0 && mCanLoadMore)){
//            loadOnStart();
        }
    }
    /**
     * 加载数据
     * @param loadMore 是否加载更多
     */
    public void load(boolean loadMore) {
//        loadmore == ture ? pageNO++ : pageNO
        load(loadMore, mArticleId);
    }
    /**
     * 在此方法创建request并加载数据
     * @param loadMore 是否加载更多
     *
     */
    public void load(boolean loadMore,int mArticleId) {
        if(mLoading || (loadMore && !mCanLoadMore)){
            return;
        }

        mLoading = true;
        mLoadingMore = loadMore;
        getListStateListener().onLoadCommentListStarted(getRequestCode());

        //文章ID
//        Long untilId = null;
//        if(loadMore && mArticleListResult != null){
//            int size = mArticleListResult.paramsData.articleLists.size();
//            if(size > 0){
//                untilId = mArticleListResult.paramsData.articleLists.get(size-1).id;
//            }
//        }
        if(!loadMore){
            pageNO = 1;
        }
        ApiRequest<CommentResult> request = ApiRequests.newCommentListResultRequest(mArticleId,pageNO);
        RequestFragment.startRequest(request,new State(loadMore,DEFAULT_COUNT_PER_LOAD),this);
    }

    @Override
    public void onVolleyResponse(int requestCode, boolean successful, CommentResult result,
                                 VolleyError error, State requestState) {
        //改变加载状态
        mLoading = false;
        mLoadingMore = false;
        //通知界面加载数据完成
        getListStateListener().onLoadCommentListFinished(getRequestCode());

        if (successful){
            CommentPage mPage = result.data.page;
            mCanLoadMore = mPage.pageNo * requestState.count < mPage.totalCount;
            ++pageNO;
            if(requestState.loadMore){
                //加载更多
                mCommentIdList = ArrayUtils.toList(mPage.list);
                Map<String,Comment> maps = new Gson().fromJson(mPage.map, new TypeToken<Map<String, Comment>>(){}.getType());
                getListStateListener().onCommentListAppended(requestCode,mCommentIdList,maps);
            }else{
                mCommentIdList = ArrayUtils.toList(mPage.list);
                Map<String,Comment> maps = new Gson().fromJson(mPage.map, new TypeToken<Map<String, Comment>>(){}.getType());
                mCommentLists = new SparseArray<>();
                for (Comment com : maps.values()) {
                    mCommentLists.put(com.id, com);
                }
                getListStateListener().onCommentListChanged(requestCode,mCommentIdList,mCommentLists);
            }
        }else {
            //失败返回error
        }

//        if(successful){
//            mCanLoadMore = result.paramsData.articleLists.size() == count;
//            if(loadMore){
//                mArticleLists.addAll(result.paramsData.articleLists);
//                //通知界面loadMore完成
//                getListStateListener().onArticleListAppended(getRequestCode(),
//                        Collections.unmodifiableList(result.paramsData.articleLists));
//                for(ArticleList articlelist : result.paramsData.articleLists){
//                    EventBusUtils.postAsync(new ArticleDesUpdatedEvent(articlelist, this));
//                }
//            }else {
//                setArticleDesList(result.paramsData.articleLists);
//            }
//        }else {
//            getListStateListener().onLoadArticleListError(getRequestCode(), error);
//        }
    }

    private ListStateListener getListStateListener(){
        return (ListStateListener) getTarget();
    }

    @Keep
    public void onEventMainThread(CommentUpdatedEvent event){
        if(event.isFromMyself(this) || mCommentLists == null){
            return;
        }

        for (int i = 0, size = mCommentLists.size(); i < size; ++i) {
            Comment comment = mCommentLists.get(i);
            boolean changed = false;
            if (comment.id == event.comment.id) {
                mCommentLists.put(comment.id, event.comment);
                changed = true;
            }
//            else if (articleList != null
//                    && articleList.id == event.articleDes.id) {
//                articleList = event.articleDes;
//                changed = true;
//            }
            if (changed) {
                getListStateListener().onCommentChanged(getRequestCode(), i ,mCommentLists.get(i));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusUtils.unregister(this);
    }
    /**
     * 标识状态类
     */
    static class State {
        public boolean loadMore;
        public int count;

        public State(boolean loadMore, int count) {
            this.loadMore = loadMore;
            this.count = count;
        }
    }
    /**
     * 加载数据状态和评论状态接口
     */
    public interface ListStateListener {
        /**
         * 回调通知 开始请求数据显示等待状态
         * @param requestCode
         */
        void onLoadCommentListStarted(int requestCode);

        /**
         * 加载评论列表数据完成了，可以更新UI啦
         * @param requestCode
         */
        void onLoadCommentListFinished(int requestCode);
        void onLoadCommentListError(int requestCode, VolleyError error);

        /**
         * 初次加载or刷新
         * @param requestCode
         * @param newIdList
         * @param newCommentMaps
         */
        void onCommentListChanged(int requestCode, ArrayList<Integer> newIdList, SparseArray<Comment> newCommentMaps);

        /**
         * 加载更多
         * @param requestCode
         * @param newIdList
         * @param newCommentMaps
         */
        void onCommentListAppended(int requestCode, ArrayList<Integer> newIdList, Map<String,Comment> newCommentMaps);

        void onCommentChanged(int requestCode, int position, Comment newComment);
        void onCommentWriteStarted(int requestCode, int position);
        void onCommentWriteFinished(int requestCode, int position);
    }
}
