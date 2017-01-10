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
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialedittext.MaterialEditText;
import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.R;
import mika.com.android.ac.account.ui.AcerSignInActivity;
import mika.com.android.ac.article.content.CommentListResource;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.PostCommentRequest;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.network.api.info.acapi.PostCommentResult;
import mika.com.android.ac.ui.AppBarWrapperLayout;
import mika.com.android.ac.ui.DetectsSoftKeyBoardFrameLayout;
import mika.com.android.ac.ui.EmotionView;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.ui.OnVerticalScrollWithPagingTouchSlopListener;
import mika.com.android.ac.ui.PagerSlidingTabStrip;
import mika.com.android.ac.util.DensityUtil;
import mika.com.android.ac.util.RecyclerViewUtils;

import static android.view.View.TRANSLATION_Y;

public class ArticleActivity2 extends AppCompatActivity implements
        ArtComplexAdapter.EventListener,
        CommentListResource.ListStateListener,
        DetectsSoftKeyBoardFrameLayout.SoftKeyBoardListener {

    private static final String[] tabs = {"AC娘", "匿名版", "新娘", "彩娘", "TD猫", "皮尔德"};
    private static final int COMMENT_HEAD_POSITION = 2;
    private static final int SCROLL_FLAG_POSITION = 8;

    @BindView(R.id.toolbar_art)
    Toolbar mToolbar;
    @BindView(R.id.art_Progress_wrap)
    RelativeLayout mProgress;
    @BindDimen(R.dimen.toolbar_height)
    int mToolbarHeight;
    @BindView(R.id.art_com_recycle)
    RecyclerView mRecycleView;
    @BindView(R.id.appBarWrapper)
    AppBarWrapperLayout mAppBarWrapperLayout;

    //底部sendBar
    @BindView(R.id.article_send_bar)
    RelativeLayout mSendBar;
    @BindView(R.id.article_send)
    ImageButton send;
    @BindView(R.id.article_insert_emoticon)
    ImageButton insertEmo;
    @BindView(R.id.article_comment_edit)
    MaterialEditText mCommentEdit;
    @BindView(R.id.article_send_progress)
    ProgressBar sendProgress;
    //表情
    @BindView(R.id.article_emoticon_tap)
    PagerSlidingTabStrip mEmoticonTab;
    @BindView(R.id.article_emoticon_pager)
    ViewPager mEmoticonPager;
    @BindView(R.id.article_emoticon_layout)
    RelativeLayout mEmoticonLayout;

    private int contentId;
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
    private PostCommentResult postCommentResult;
    private MenuItem menuStar;
    private MenuItem menuUnquote;
    private boolean commentFirstClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        TransitionUtils.setupTransitionBeforeDecorate(this);
        super.onCreate(savedInstanceState);

        createUI();
        mCommentListResource = CommentListResource.attachTo(null, contentId, this);
    }

    private void createUI() {
        setContentView(R.layout.activity_article_2);
//        TransitionUtils.setupTransitionAfterSetContentView(this);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (DetectsSoftKeyBoardFrameLayout) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        mainLayout.setSoftKeyBoardListener(this);
        // intent from comment
        if(Intent.ACTION_VIEW.equalsIgnoreCase(getIntent().getAction()) && getIntent().getData() != null
                && getIntent().getData().getScheme().equals("a_ac")){
            contentId = Integer.parseInt(getIntent().getDataString().substring(9));
        }else {
            contentId = getIntent().getExtras().getInt("aid", 0);
        }

        setTitle(getIntent().getExtras().getString("title", "ac/" + contentId));
        //title style
        mToolbar.setTitleTextAppearance(this, R.style.Base_TextAppearance_AppCompat_ActivityTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setPaddingTop();
        initRecycleView();
        initSendBar();
    }

    /**
     * 底部评论bar 事件
     */
    private void initSendBar() {
        //send button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendComment();
            }
        });
        //emoticon button
        insertEmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmoticonLayoutShow && !isKeyBoardShow) {
                    isEmoticonLayoutShow = true;
                    mEmoticonLayout.setVisibility(View.VISIBLE);
                } else if (!isEmoticonLayoutShow) {
                    isEmoticonLayoutShow = true;
                    isKeyBoardShow = false;
                    inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
                    inputMethodManager.toggleSoftInputFromWindow(mainLayout.getApplicationWindowToken(),
                            InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
        mEmoticonPager.setAdapter(new EmoticonPagerAdapter());
        mEmoticonTab.setViewPager(mEmoticonPager);
    }

    /**
     * set recyclerView
     */
    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new NoChangeAnimationItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mArtComplexAdapter = new ArtComplexAdapter(initList(), this, getIntent().getExtras(), contentId);
        mArtComplexAdapter.setEventListener(this);
        mLoadMoreAdapter = new LoadMoreAdapter(R.layout.load_more_card_item, mArtComplexAdapter);
        mLoadMoreAdapter.setItemVisible(false);
        mRecycleView.setAdapter(mLoadMoreAdapter);
    }

    /**
     * 进度条消失后，为recycleView添加ScrollListener
     */
    @Override
    public void ProgressDismiss() {

        mRecycleView.addOnScrollListener(new OnVerticalScrollWithPagingTouchSlopListener(this) {
            //recyclerView向下滑动
            @Override
            public void onScrolledDown() {
                if (RecyclerViewUtils.hasFirstChildReachedTop(mRecycleView)) {
                    mAppBarWrapperLayout.hide();
                }
                if (!isEmoticonLayoutShow && !isKeyBoardShow && !isSending && !isEditing) {
                    sendBarHide(mSendBar);
                }
                // auto load more
                if (((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition()
                        == mRecycleView.getAdapter().getItemCount() - 1) {
                    mArtComplexAdapter.setAutoLoad(false);
                    loadComment();
                    mLoadMoreAdapter.setItemVisible(true);
                }
            }

            @Override
            public void onScrolledUp() {
                mAppBarWrapperLayout.show();
                sendBarShow(mSendBar);
            }

            @Override
            public void onScrolled(int dy) {
                if (!RecyclerViewUtils.hasFirstChildReachedTop(mRecycleView)) {
                    mAppBarWrapperLayout.show();
                }
            }

            @Override
            public void onScrolledToBottom() {

            }
        });

        mProgress.setVisibility(View.GONE);
        mLoadMoreAdapter.setItemVisible(true);
        // if article high < screen high , load comment
//        if (((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition()
//                == mRecycleView.getAdapter().getItemCount() - 1) {
//            mArtComplexAdapter.setAutoLoad(false);
//            loadComment();
//            mLoadMoreAdapter.setItemVisible(true);
//        }
    }

    /**
     * 先给文章head 内容 和评论标题 加到list中
     */
    private List<Integer> initList() {
        List<Integer> list = new ArrayList<>();
        list.add(AcWenApplication.ITEM_HEAD);
        list.add(AcWenApplication.ITEM_ARTICLE);
        list.add(AcWenApplication.ITEM_SUBTITLE);
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article, menu);
        menuStar = menu.findItem(R.id.action_star);
        setStarImage(menuStar);
        menuUnquote = menu.findItem(R.id.action_unquote);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_star:
                // TODO 收藏
                return true;
            case R.id.action_top:
                if (((LinearLayoutManager) mRecycleView.getLayoutManager()).findFirstVisibleItemPosition() > SCROLL_FLAG_POSITION) {
                    mRecycleView.scrollToPosition(SCROLL_FLAG_POSITION);
                    mRecycleView.smoothScrollToPosition(COMMENT_HEAD_POSITION);
                } else {
                    mRecycleView.smoothScrollToPosition(3);
                }
                return true;
            case R.id.action_unquote:
                menuUnquote.setVisible(false);
                unQuote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void unQuote() {
        isEditing = false;
        quote = null;
        mCommentEdit.setText("");
        mCommentEdit.setHint(R.string.article_send_comment_hint);
        Snackbar.make(getWindow().getDecorView(), R.string.article_send_quote_cancel, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 设置menu 收藏图标
     */
    private void setStarImage(MenuItem menuStar) {
        // TODO 修改图标状态
//        menuStar.setIcon(ContextCompat.getDrawable(this,R.drawable.star_icon_white_24dp));
    }

    /**
     * 发送评论按钮调用方法
     */
    private void doSendComment() {

        if (!AcWenApplication.LOGIN) {
            startActivityForResult(new Intent(this, AcerSignInActivity.class), AcWenApplication.REQUEST_CODE_SIGN_IN);
            return;
        }

        if (!isCTextOk()) {
            return;
        }

        if (quote == null)
            quote = new Comment();
        changeSendState(true);
        PostCommentRequest postCommentRequest = new PostCommentRequest(getCommentText(), quote.id,
                AcWenApplication.getInstance().getAcer().access_token, AcWenApplication.getInstance().getAcer().userId, contentId);
        setPostListener(postCommentRequest);
        Volley.getInstance().addToRequestQueue(postCommentRequest);
    }

    /**
     * post response listener
     *
     * @param postCommentRequest request
     */
    private void setPostListener(PostCommentRequest postCommentRequest) {
        postCommentRequest.setListener(new Response.Listener<PostCommentResult>() {
            @Override
            public void onResponse(PostCommentResult response) {
                quote = null;
                postCommentResult = response;
                if (postCommentResult.success) {
                    Toast.makeText(ArticleActivity2.this, R.string.post_comment_success, Toast.LENGTH_SHORT).show();
                    changeSendState(false);
                    updateComment();
                } else {
                    Toast.makeText(ArticleActivity2.this, R.string.post_comment_may_failed, Toast.LENGTH_LONG).show();
                    changeSendState(false);
                }
            }
        });
        postCommentRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                quote = null;
                Toast.makeText(ArticleActivity2.this, R.string.post_comment_may_failed, Toast.LENGTH_LONG).show();
                changeSendState(false);
            }
        });
    }

    /**
     * 改变发送评论时的状态
     */
    private void changeSendState(boolean sending) {
        isSending = sending;
        send.setVisibility(sending ? View.GONE : View.VISIBLE);
        sendProgress.setVisibility(sending ? View.VISIBLE : View.GONE);
        if (sending) {
            if (isEmoticonLayoutShow) {
                isEmoticonLayoutShow = false;
                mEmoticonLayout.setVisibility(View.GONE);
            }
            if (isKeyBoardShow) {
                inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        } else {
            if (postCommentResult != null && postCommentResult.success) { // 发送成功
                isEditing = false;
                menuUnquote.setVisible(false);
                mCommentEdit.setText("");
                mCommentEdit.setHint(R.string.article_send_comment_hint);
            }
        }
    }

    /**
     * 加载评论
     */
    public void loadComment() {
        if (isFirstLoad) {
            isFirstLoad = false;
            mCommentListResource.load(false);
        } else {
            mCommentListResource.load(true);
        }
    }

    /**
     * 输入评论/引用评论
     */
    @Override
    public void insertComment(Comment quote) {
        if (!isSendBarShowing)
            sendBarShow(mSendBar);
        mCommentEdit.setHint(String.format(Locale.getDefault(), getResources().getString(R.string.article_send_quote), quote.username));
        menuUnquote.setVisible(true);
        isEditing = true;
        this.quote = quote;
    }

    /**
     * 评论post请求 的text参数
     */
    private String getCommentText() {
        Editable text = SpannableStringBuilder.valueOf(mCommentEdit.getText());
        return text.toString();
    }

    /**
     * 检查评论是否合法
     */
    private boolean isCTextOk() {
        Editable text = SpannableStringBuilder.valueOf(mCommentEdit.getText());
        Object[] spans = text.getSpans(0, text.length(), ImageSpan.class);
        int le = text.toString().length() - spans.length * 14;
        if (le < 5) {
            Toast.makeText(this, R.string.comment_content_illegal, Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void updateComment() {
        mCommentListResource.load(false);
    }

    @Override
    public void dataReplaceOk() {
        // scroll to comment head
        mRecycleView.scrollToPosition(COMMENT_HEAD_POSITION);
    }

    @Override
    public void onArticleFailed() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onArticleReload() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArtComplexAdapter.resume();
    }

    private void sendBarShow(RelativeLayout sendBar) {
        if (isSendBarShowing) {
            return;
        }
        isSendBarShowing = true;
        clearAnimator();
        mAnimator = new AnimatorSet().setDuration(200);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());
//        向上平移
        ObjectAnimator objectAnimator = new ObjectAnimator();
        mAnimator.play(objectAnimator.ofFloat(sendBar, TRANSLATION_Y, DensityUtil.dip2px(this, sendBar.getHeight()), 0));
        mAnimator.start();

    }

    private void sendBarHide(RelativeLayout sendBar) {
        if (!isSendBarShowing) {
            return;
        }
        isSendBarShowing = false;

        clearAnimator();
        mAnimator = new AnimatorSet().setDuration(200);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());
//        向下平移
        mAnimator.play(new ObjectAnimator().ofFloat(sendBar, TRANSLATION_Y, 0, DensityUtil.dip2px(this, sendBar.getHeight())));
        mAnimator.start();
    }

    @Override
    protected void onStop() {
        mArtComplexAdapter.pause();

        if (isKeyBoardShow) {
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (isEmoticonLayoutShow) {
            isEmoticonLayoutShow = false;
            mEmoticonLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void clearAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    public void onLoadCommentListStarted(int requestCode) {
        mLoadMoreAdapter.onLoadStarted();
    }

    @Override
    public void onLoadCommentListFinished(int requestCode) {
        if (((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition() <=
                mRecycleView.getAdapter().getItemCount() - COMMENT_HEAD_POSITION) {
            mLoadMoreAdapter.setItemVisible(false);
        }
        mLoadMoreAdapter.setNoMoreYet(!mCommentListResource.canLoadMore() && !mCommentListResource.isEmpty());
        mLoadMoreAdapter.setProgressVisible(false);
        mArtComplexAdapter.setUpdataProgressGONE();
    }

    @Override
    public void onLoadCommentListError(int requestCode, VolleyError error) {
        mLoadMoreAdapter.setItemText(getString(R.string.load_more_failed));
    }

    @Override
    public void onCommentListChanged(int requestCode, ArrayList<Integer> newIdList, SparseArray<Comment> newCommentMaps) {
        mArtComplexAdapter.replace(newIdList, newCommentMaps);
    }

    /**
     * using
     */
    @Override
    public void onCommentListAppended(int requestCode, ArrayList<Integer> newIdList, Map<String, Comment> newCommentMaps) {
        mArtComplexAdapter.insert(newIdList, newCommentMaps);
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
    public void onSoftKeyBoardDown(boolean isShowing) {
        isKeyBoardShow = isShowing;
    }

    private void setPaddingTop() {
        mRecycleView.setPadding(mRecycleView.getPaddingLeft(), mToolbarHeight,
                mRecycleView.getPaddingRight(), mRecycleView.getPaddingBottom());
    }

    /**
     * 表情条目点击事件内部类
     */
    private class AcEmoticonGridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int index = mCommentEdit.getSelectionEnd();
            Editable text = mCommentEdit.getText();
            String emoticon = parent.getItemAtPosition(position).toString();
            text.insert(index, emoticon);
            Drawable d = ((EmotionView) parent.getAdapter().getView(position, null, null)).getmDrawable();
            d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
            text.setSpan(new ImageSpan(d), index, index + emoticon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 表情页面适配器内部类
     */
    private class EmoticonPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position % tabs.length];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = (GridView) getLayoutInflater().inflate(R.layout.article_emoticon_grid, null);

            gridView.setAdapter(new EmoticonGridAdapter(ArticleActivity2.this, position));
            gridView.setOnItemClickListener(new AcEmoticonGridItemClickListener());

            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
