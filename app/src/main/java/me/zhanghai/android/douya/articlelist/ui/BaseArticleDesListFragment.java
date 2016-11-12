package me.zhanghai.android.douya.articlelist.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.article.ui.ArticleActivity2;
import me.zhanghai.android.douya.articlelist.content.ArticleListResFragment;
import me.zhanghai.android.douya.main.ui.MainActivity;
import me.zhanghai.android.douya.network.api.ApiError;
import me.zhanghai.android.douya.network.api.info.acapi.Acer;
import me.zhanghai.android.douya.network.api.info.acapi.ArticleList;
import me.zhanghai.android.douya.ui.AppBarManager;
import me.zhanghai.android.douya.ui.FriendlyFloatingActionButton;
import me.zhanghai.android.douya.ui.FriendlySwipeRefreshLayout;
import me.zhanghai.android.douya.ui.LoadMoreAdapter;
import me.zhanghai.android.douya.ui.NoChangeAnimationItemAnimator;
import me.zhanghai.android.douya.ui.OnVerticalScrollWithPagingTouchSlopListener;
import me.zhanghai.android.douya.util.CardUtils;
import me.zhanghai.android.douya.util.LogUtils;
import me.zhanghai.android.douya.util.RecyclerViewUtils;
import me.zhanghai.android.douya.util.ToastUtil;
import me.zhanghai.android.douya.util.ViewUtils;

/**
 * 抽象文章列表资源Fragment基类
 * Created by Administrator on 2016/9/6.
 */

public abstract class BaseArticleDesListFragment extends Fragment implements
        ArticleListResFragment.ListStateListener, ArticleDesAdapter.OnBtnClickedListener{

    @BindView(R.id.swipe_refresh_artdes)
    FriendlySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.article_list)
    RecyclerView mArticleDesList;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.gotop)
    FriendlyFloatingActionButton mGoTopFab;

    private ArticleListResFragment mArticleListResource;

    private ArticleDesAdapter mArticleDesAdapter;
    private LoadMoreAdapter mLoadMoreAdapter;
    private Acer mAcer;

    protected BaseArticleDesListFragment () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ViewUtils.inflate(R.layout.articledes_list_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        CustomTabsHelperFragment.attachTo(this);

        mArticleListResource = onAttachArticleListResource();

        mAcer = ((MainActivity)activity).getAcer();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });
        //recycleView
        mArticleDesList.setHasFixedSize(true);
        mArticleDesList.setItemAnimator(new NoChangeAnimationItemAnimator());
        mArticleDesList.setLayoutManager(new StaggeredGridLayoutManager(
                CardUtils.getColumnCount(activity), StaggeredGridLayoutManager.VERTICAL));
        //adapter
        mArticleDesAdapter = new ArticleDesAdapter(mArticleListResource.get(), this);
        mLoadMoreAdapter = new LoadMoreAdapter(R.layout.load_more_card_item, mArticleDesAdapter);
        mArticleDesList.setAdapter(mLoadMoreAdapter);
        //recycleView事件
        final AppBarManager appBarManager = (AppBarManager) getParentFragment();
        mArticleDesList.addOnScrollListener(
                new OnVerticalScrollWithPagingTouchSlopListener(activity) {
            @Override
            public void onScrolled(int dy) {
                if(!RecyclerViewUtils.hasFirstChildReachedTop(mArticleDesList)){
                    onShow();
                }
            }

            @Override
            public void onScrolledUp() {
                onShow();
            }

            private void onShow(){
                appBarManager.showAppBar();
                //gotop按钮show
            }

            @Override
            public void onScrolledDown() {
                if(RecyclerViewUtils.hasFirstChildReachedTop(mArticleDesList)){
                    appBarManager.hideAppBar();
                    //gotop按钮hide
                }
            }

            @Override
            public void onScrolledToBottom() {
                mArticleListResource.load(true);
            }
        });
        upDateRefreshing();

//        CheatSheetUtils.setup(mGoTopFab);
//        mGoTopFab.setOnClickListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mArticleListResource.detach();
    }

    private void upDateRefreshing(){
        boolean loading = mArticleListResource.isLoading();
        boolean loadingMore = mArticleListResource.isLoadingMore();
        boolean empty = mArticleListResource.isEmpty();
        mSwipeRefreshLayout.setRefreshing(loading && (!empty || mSwipeRefreshLayout.isRefreshing())
                && !loadingMore);
        ViewUtils.setVisibleOrGone(mProgress, loading && empty);
        mLoadMoreAdapter.setProgressVisible(loading && !empty &&loadingMore);
    }

    protected void onSwipeRefresh(){
        mArticleListResource.load(false);
    }

    protected void setPaddingTop(int paddingTop){
        mSwipeRefreshLayout.setProgressViewOffset(paddingTop);
        mArticleDesList.setPadding(mArticleDesList.getPaddingLeft(),paddingTop,
                mArticleDesList.getPaddingRight(), mArticleDesList.getPaddingBottom());
    }

    protected abstract ArticleListResFragment onAttachArticleListResource();

    @Override
    public void onOpenArticle(ArticleList articleList, View sharedView) {
        //打开文章界面
        openArticle(articleList, sharedView, false);
    }

    private void openArticle(ArticleList articleList, View sharedView, boolean b) {
        //打开文章
        Intent intent = new Intent(getActivity(), ArticleActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putInt("aid",articleList.id);
        bundle.putString("title",articleList.title);
        bundle.putString("username",articleList.username);
        bundle.putString("time",articleList.diffTime);
        bundle.putLong("view_count",articleList.viewCount);
        bundle.putString("avatar",articleList.userAvatar);
        //如果是登陆状态，把这个也传过去
        if(DouyaApplication.LOGIN){
            bundle.putString("access_token",mAcer.access_token);
            bundle.putInt("userId",mAcer.userId);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void scrollToTop(){
        if(mArticleDesList != null && mArticleDesList.getAdapter() != null){
            if(((StaggeredGridLayoutManager)mArticleDesList.getLayoutManager()).findFirstVisibleItemPositions(null)[0] > 5){
                mArticleDesList.scrollToPosition(5);
                mArticleDesList.smoothScrollToPosition(0);
            }else{
                mArticleDesList.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void onStartClicked(ArticleList articleList, boolean start) {
        //收藏……个屁啊
    }

    @Override
    public void onBananaClicked(ArticleList articleList, boolean Bananaed) {

    }

    @Override
    public void onCommentClicked(ArticleList articleList, View sharedView) {
        //直接跳转到评论界面，评论
    }

    @Override
    public void onLoadArticleListStarted(int requestCode) {
        upDateRefreshing();
    }

    @Override
    public void onLoadArticleListFinished(int requestCode) {
        upDateRefreshing();
    }

    @Override
    public void onLoadArticleListError(int requestCode, VolleyError error) {
        //加载失败，返回信息
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onArticleListChanged(int requestCode, List<ArticleList> newArticleList) {
        mArticleDesAdapter.replace(newArticleList);
    }

    @Override
    public void onArticleListAppended(int requestCode, List<ArticleList> appendedArticleList) {
        mArticleDesAdapter.addAll(appendedArticleList);
    }

    @Override
    public void onArticleChanged(int requestCode, int position, ArticleList newArticleDes) {
        mArticleDesAdapter.set(position,newArticleDes);
    }

    @Override
    public void onArticleRemoved(int requestCode, int position) {

    }

    @Override
    public void onArticleWriteStarted(int requestCode, int position) {

    }

    @Override
    public void onArticleWriteFinished(int requestCode, int position) {

    }
}
