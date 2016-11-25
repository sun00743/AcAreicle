/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.article.ui;


import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.article.content.CommentListResource;
import mika.com.android.ac.network.api.PostCommentRequest;
import mika.com.android.ac.network.api.info.acapi.Acer;
import mika.com.android.ac.network.api.info.acapi.ArticleList;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.network.api.info.acapi.PostCommentResult;
import mika.com.android.ac.ui.AppBarWrapperLayout;
import mika.com.android.ac.ui.DetectsSoftKeyBoardFrameLayout;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.TransitionUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment implements
        ArtComplexAdapter.EventListener,
        CommentListResource.ListStateListener,
        DetectsSoftKeyBoardFrameLayout.SoftKeyBoardListener {

    private static final String[] tabs = {"AC娘", "(=ﾟωﾟ)="};

    @BindView(R.id.toolbar_art)
    Toolbar mToolbar;

    @BindView(R.id.art_Progress_wrap)
    RelativeLayout mProgressWrap;
    @BindView(R.id.art_Progress)
    ProgressBar mProgress;
    @BindDimen(R.dimen.toolbar_height)
    int mToolbarHeight;
    @BindView(R.id.art_com_recycle)
    RecyclerView mRecycleView;
    @BindView(R.id.appBarWrapper)
    AppBarWrapperLayout mAppBarWrapperLayout;

    @BindView(R.id.shared)
    View mSharedView;
    @BindView(R.id.container)
    FrameLayout mContainerLayout;

    //底部sendBar
//    @BindView(R.id.article_send_bar)
//    RelativeLayout mSendBar;
//    @BindView(R.id.article_send)
//    ImageButton send;
//    @BindView(R.id.article_insert_emoticon)
//    ImageButton insertEmo;
//    @BindView(R.id.article_comment_edit)
//    MaterialEditText mCommentEdit;
//    @BindView(R.id.article_send_progress)
//    ProgressBar sendProgress;
//    //表情
//    @BindView(R.id.article_emoticon_tap)
//    PagerSlidingTabStrip mEmoticonTab;
//    @BindView(R.id.article_emoticon_pager)
//    ViewPager mEmoticonPager;
//    @BindView(R.id.article_emoticon_layout)
//    RelativeLayout mEmoticonLayout;

    private int contentId;
    private Acer acer;
    private AnimatorSet mAnimator;
    private boolean isSendBarShowing = true;
    private boolean isFirstLoad = true;
    private CommentListResource mCommentListResource;
    private ArtComplexAdapter mArtComplexAdapter;
    private LoadMoreAdapter mLoadMoreAdapter;
    private boolean isEmoticonLayoutShow = false;
    private boolean isKeyBoardShow = false;
    private boolean isSending = false;
    private boolean isEditing = false;
    private DetectsSoftKeyBoardFrameLayout mainLayout;
    private InputMethodManager inputMethodManager;
    private Comment quote;
    private PostCommentRequest postCommentRequest;
    private PostCommentResult postCommentResult;
    private MenuItem menuStar;
    private MenuItem menuUnquote;
    private ArticleList articleDes;


    public static ArticleFragment newInstance(Bundle bundle, ArticleList articleDes) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle arguments = FragmentUtils.ensureArguments(articleFragment);
        arguments.putBundle("extras", bundle);
        arguments.putParcelable("articleDes", articleDes);
        return articleFragment;
    }

    public ArticleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        articleDes = arguments.getParcelable("articleDes");

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCommentListResource = CommentListResource.attachTo(null, articleDes.id, this);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(articleDes.title);
        activity.setSupportActionBar(mToolbar);

        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(activity);
            }
        });

        // This magically gives better visual effect when the broadcast is partially visible. Using
        // setEnterSharedElementCallback() disables this hack when no transition is used to start
        // this Activity.
        ActivityCompat.setEnterSharedElementCallback(activity, new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {
                mRecycleView.scrollTo(0,0);
//                mRecycleView.scrollToPosition(0);
            }
        });

        setPaddingTop();
        initRecycleView();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArtComplexAdapter.getArticleView().setVisibility(View.GONE);
                ActivityCompat.finishAfterTransition(activity);
            }
        });

        TransitionUtils.setEnterReturnExplode(this);
        TransitionUtils.setupTransitionOnActivityCreated(this);
//        initSendBar();

        inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        mainLayout = (DetectsSoftKeyBoardFrameLayout) ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
//        mainLayout.setSoftKeyBoardListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setPaddingTop() {
        mRecycleView.setPadding(mRecycleView.getPaddingLeft(), mToolbarHeight,
                mRecycleView.getPaddingRight(), mRecycleView.getPaddingBottom());
    }

    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new NoChangeAnimationItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArtComplexAdapter = new ArtComplexAdapter(initList(), getActivity(), getArguments().getBundle("extras"));
        mArtComplexAdapter.setEventListener(this);
        mLoadMoreAdapter = new LoadMoreAdapter(R.layout.load_more_card_item, mArtComplexAdapter);
        mRecycleView.setAdapter(mLoadMoreAdapter);
    }

    /**
     * 先给文章head 内容 和评论标题 加到list中
     *\
     */
    private List<Integer> initList() {
        List<Integer> list = new ArrayList<>();
        list.add(AcWenApplication.ITEM_HEAD);
        list.add(AcWenApplication.ITEM_ARTICLE);
        list.add(AcWenApplication.ITEM_SUBTITLE);
        return list;
    }

/*
    */
/**
     * 底部评论bar 事件
     *//*

    private void initSendBar() {
//        发送按钮
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                doSendComment();
            }
        });
//        表情按钮
        insertEmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmoticonLayoutShow && !isKeyBoardShow) {
                    isEmoticonLayoutShow = true;
                    mEmoticonLayout.setVisibility(View.VISIBLE);
                } else if (!isEmoticonLayoutShow) {
                    isEmoticonLayoutShow = true;
                    isKeyBoardShow = false;
                    inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmoticonLayout.setVisibility(View.VISIBLE);
                        }
                    }, 70);
                } else if (!isKeyBoardShow) {
                    isKeyBoardShow = true;
                    isEmoticonLayoutShow = false;
                    mEmoticonLayout.setVisibility(View.GONE);
                    mCommentEdit.requestFocus();
                    inputMethodManager.toggleSoftInputFromWindow(
                            mainLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        // edit 点击时如果 emoticonLayout显示，则隐藏
        mCommentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmoticonLayoutShow) {
                    isEmoticonLayoutShow = false;
                    mEmoticonLayout.setVisibility(View.GONE);
                }
            }
        });
//        mEmoticonPager.setAdapter(new ArticleActivity2.EmoticonPagerAdapter());
        mEmoticonTab.setViewPager(mEmoticonPager);
    }
*/

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoadCommentListStarted(int requestCode) {

    }

    @Override
    public void onLoadCommentListFinished(int requestCode) {

    }

    @Override
    public void onLoadCommentListError(int requestCode, VolleyError error) {

    }

    @Override
    public void onCommentListChanged(int requestCode, ArrayList<Integer> newIdList, SparseArray<Comment> newCommentMaps) {

    }

    @Override
    public void onCommentListAppended(int requestCode, ArrayList<Integer> newIdList, Map<String, Comment> newCommentMaps) {

    }

    @Override
    public void onCommentChanged(int requestCode, int position, Comment newComment) {

    }

    @Override
    public void onCommentWriteStarted(int requestCode, int position) {

    }

    @Override
    public void onCommentWriteFinished(int requestCode, int position) {

    }

    @Override
    public void ProgressDismiss() {

    }

    @Override
    public void insertComment(Comment quote) {

    }

    @Override
    public void updateComment() {

    }

    @Override
    public void DataReplaceOk() {

    }

    @Override
    public void onSoftKeyBoardDown(boolean isShowing) {

    }

    @Override
    public void setHeadView(View headview) {
//        ViewCompat.setTransitionName(headview, ArticleList.makeTransitionName(articleDes.id));
    }
}
