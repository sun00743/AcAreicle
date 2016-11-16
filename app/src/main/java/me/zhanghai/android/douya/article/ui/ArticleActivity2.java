package me.zhanghai.android.douya.article.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.DouyaApplication;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.account.ui.AcerSignInActivity;
import me.zhanghai.android.douya.article.content.CommentListResource;
import me.zhanghai.android.douya.network.Volley;
import me.zhanghai.android.douya.network.api.PostCommentRequest;
import me.zhanghai.android.douya.network.api.info.acapi.Acer;
import me.zhanghai.android.douya.network.api.info.acapi.Comment;
import me.zhanghai.android.douya.network.api.info.acapi.PostCommentResult;
import me.zhanghai.android.douya.ui.AppBarWrapperLayout;
import me.zhanghai.android.douya.ui.DetectsSoftKeyBoardFrameLayout;
import me.zhanghai.android.douya.ui.EmotionView;
import me.zhanghai.android.douya.ui.LoadMoreAdapter;
import me.zhanghai.android.douya.ui.NoChangeAnimationItemAnimator;
import me.zhanghai.android.douya.ui.OnVerticalScrollWithPagingTouchSlopListener;
import me.zhanghai.android.douya.ui.PagerSlidingTabStrip;
import me.zhanghai.android.douya.util.DensityUtil;
import me.zhanghai.android.douya.util.RecyclerViewUtils;
import me.zhanghai.android.douya.util.ToastUtil;
import me.zhanghai.android.materialedittext.MaterialEditText;

import static android.view.View.TRANSLATION_Y;

public class ArticleActivity2 extends AppCompatActivity implements
        ArtComplexAdapter.EventListener,
        CommentListResource.ListStateListener,
        DetectsSoftKeyBoardFrameLayout.SoftKeyBoardListener {

    private static final String[] tabs = {"AC娘", "(=ﾟωﾟ)="};

    @BindView(R.id.toolbar_art)
    Toolbar mToolbar;
    @BindView(R.id.art_Progress_wrap)
    RelativeLayout mProgress;
    @BindDimen(R.dimen.toolbar_height)
    int mToolbarHeight;
    @BindView(R.id.art_com_recycle)
    RecyclerView mRecycleView;
    @BindView(R.id.appBarWrapper_art)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
        acer = DouyaApplication.getInstance().getAcer();
        contentId = getIntent().getExtras().getInt("aid", 0);
        mCommentListResource = CommentListResource.attachTo(null, contentId, this);
    }

    private void createUI() {
        setContentView(R.layout.activity_article_2);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (DetectsSoftKeyBoardFrameLayout) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        mainLayout.setSoftKeyBoardListener(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getIntent().getExtras().getString("title" , "ac/" + contentId));
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
//        发送按钮
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendComment();
            }
        });
//        表情按钮
        insertEmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmoticonLayoutShow && !isKeyBoardShow) {
                    isEmoticonLayoutShow = true;
                    mEmoticonLayout.setVisibility(View.VISIBLE);
                } else if (!isEmoticonLayoutShow && isKeyBoardShow) {
                    isEmoticonLayoutShow = true;
                    isKeyBoardShow = false;
                    inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmoticonLayout.setVisibility(View.VISIBLE);
                        }
                    }, 70);
                } else if (isEmoticonLayoutShow && !isKeyBoardShow) {
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
        mEmoticonPager.setAdapter(new EmoticonPagerAdapter());
        mEmoticonTab.setViewPager(mEmoticonPager);
    }

    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new NoChangeAnimationItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mArtComplexAdapter = new ArtComplexAdapter(initList(), this, getIntent().getExtras());
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
        list.add(DouyaApplication.ITEM_HEAD);
        list.add(DouyaApplication.ITEM_ARTICLE);
        list.add(DouyaApplication.ITEM_SUBTITLE);
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article,menu);
        menuStar = menu.findItem(R.id.action_star);
        setStarImage(menuStar);
        menuUnquote = menu.findItem(R.id.action_unquote);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_star:
                // TODO 收藏
                return true;
            case R.id.action_top:
                if(((LinearLayoutManager)mRecycleView.getLayoutManager()).findFirstVisibleItemPosition() >= 2 && ((LinearLayoutManager)mRecycleView.getLayoutManager()).findFirstVisibleItemPosition() < 12){
                    mRecycleView.smoothScrollToPosition(2);
                }else if(((LinearLayoutManager)mRecycleView.getLayoutManager()).findFirstVisibleItemPosition() >= 12){
                    mRecycleView.scrollToPosition(4);
                    mRecycleView.smoothScrollToPosition(2);
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
        mCommentEdit.setText("");
        mCommentEdit.setHint("发评论...");
        ToastUtil.show("( ´_ゝ`) 已取消引用",this);
    }

    /**
     * 设置menu 收藏图标
     */
    private void setStarImage(MenuItem menuStar) {
        // TODO 修改图标状态
//        menuStar.setIcon(ContextCompat.getDrawable(this,R.drawable.star_icon_white_24dp));
    }

    /**
     * 进度条消失后，为recycleView添加ScrollListener
     */
    @Override
    public void ProgressDismiss() {
        mProgress.setVisibility(View.GONE);

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
                //显示progress
                LoadComment();
            }
        });
    }

    /**
     * 发送评论
     */
    private void doSendComment() {

        if (!DouyaApplication.LOGIN) {
            startActivityForResult(new Intent(this, AcerSignInActivity.class), DouyaApplication.REQUEST_CODE_SIGN_IN);
            return;
        }

        if (!isCTextOk()) {
            return;
        }

        if(quote == null)
            quote = new Comment();
        changeSendState(true);
        postCommentRequest = new PostCommentRequest(getCommentText(), quote.id, acer.access_token, acer.userId, contentId);
        setPostListener();
        Volley.getInstance().addToRequestQueue(postCommentRequest);
    }

    private void setPostListener() {
        postCommentRequest.setListener(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                quote = null;
                postCommentResult = new Gson().fromJson((String)response, PostCommentResult.class);
                if(postCommentResult.success){
                    Toast.makeText(ArticleActivity2.this, "评论成功! (＾o＾)ﾉ", Toast.LENGTH_SHORT).show();
                    changeSendState(false);
                    updateComment();
                }else{
                    Toast.makeText(ArticleActivity2.this, "(|||ﾟдﾟ) 服务器抽风，刷新看看吧", Toast.LENGTH_SHORT).show();
                    changeSendState(false);
                }
            }
        });
        postCommentRequest.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                quote = null;
                Toast.makeText(ArticleActivity2.this, "服务器抽风，评论失败! (|||ﾟдﾟ)", Toast.LENGTH_SHORT).show();
                changeSendState(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == DouyaApplication.REQUEST_CODE_SIGN_IN) {
                acer = DouyaApplication.getInstance().getAcer();
            }
        }
    }

    /**
     * 改变发送评论时的状态
     */
    private void changeSendState(boolean sending) {
        isSending = sending;
        send.setVisibility(sending ? View.GONE : View.VISIBLE);
        sendProgress.setVisibility(sending ? View.VISIBLE : View.GONE);
        if(sending){
            if(isEmoticonLayoutShow){
                isEmoticonLayoutShow = false;
                mEmoticonLayout.setVisibility(View.GONE);
            }
            if(isKeyBoardShow){
                inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        }else{
            if(postCommentResult != null && postCommentResult.success){
                isEditing = false;
                mCommentEdit.setText("");
                mCommentEdit.setHint("发评论...");
            }
        }
    }

    /**
     * 加载评论
     */
    private void LoadComment() {
        if (isFirstLoad) {
            isFirstLoad = false;
            mCommentListResource.load(false);
        } else {
            mCommentListResource.load(true);
        }
    }

    /**
     * 输入评论/引用评论
     *
     */
    @Override
    public void insertComment(Comment quote) {
        if (!isSendBarShowing)
            sendBarShow(mSendBar);
        mCommentEdit.setHint("引用" + quote.username + "的评论...");
        menuUnquote.setVisible(true);
        isEditing = true;
        this.quote = quote;
    }

    /**
     * 评论post请求 的text参数
     *
     */
    private String getCommentText() {
        Editable text = SpannableStringBuilder.valueOf(mCommentEdit.getText());
        return text.toString();
    }

    /**
     * 检查评论是否合法
     *
     */
    private boolean isCTextOk() {
        Editable text = SpannableStringBuilder.valueOf(mCommentEdit.getText());
        Object[] spans = text.getSpans(0, text.length(), ImageSpan.class);
        int le = text.toString().length() - spans.length * 14;
        if (le < 5) {
            Toast.makeText(this, "小兄弟，评论不能少于5个字还不能纯表情 (｀･ω･)", Toast.LENGTH_LONG).show();
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
    public void DataReplaceOk() {
        mRecycleView.scrollToPosition(2);
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
        AnimatorSet.Builder animBuilder = mAnimator.play(
                new ObjectAnimator().ofFloat(sendBar, TRANSLATION_Y, DensityUtil.dip2px(this, sendBar.getHeight()),
                        0));
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
        AnimatorSet.Builder animBuilder = mAnimator.play(
                new ObjectAnimator().ofFloat(sendBar, TRANSLATION_Y, 0, DensityUtil.dip2px(this, sendBar.getHeight())));
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
//        updateRefreshing();
        mLoadMoreAdapter.setProgressVisible(true);
    }

    @Override
    public void onLoadCommentListFinished(int requestCode) {
//        updateRefreshing();
        mLoadMoreAdapter.setProgressVisible(false);
    }

    @Override
    public void onLoadCommentListError(int requestCode, VolleyError error) {

    }

    @Override
    public void onCommentListChanged(int requestCode, ArrayList<Integer> newIdList, SparseArray<Comment> newCommentMaps) {
        mArtComplexAdapter.replace(newIdList, newCommentMaps);
    }

    @Override
    public void onCommentListAppended(int requestCode, ArrayList<Integer> newIdList, Map<String,Comment> newCommentMaps) {
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

    /**
     * 更新刷新状态
     */
    private void updateRefreshing() {
        boolean loading = mCommentListResource.isLoading();
        boolean empty = mCommentListResource.isEmpty();
        boolean loadingMore = mCommentListResource.isLoadingMore();
//        ViewUtils.setVisibleOrGone(mProgress, loading && empty);
        mLoadMoreAdapter.setProgressVisible(loading && !empty && loadingMore);
    }

    private void setPaddingTop() {
        mRecycleView.setPadding(mRecycleView.getPaddingLeft(), mToolbarHeight,
                mRecycleView.getPaddingRight(), mRecycleView.getPaddingBottom());
    }

    private class AcEmoticonGridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int index = mCommentEdit.getSelectionEnd();
            Editable text = mCommentEdit.getText();
            String emoticon = parent.getItemAtPosition(position).toString();
            text.insert(index, emoticon);
            EmotionView ev = (EmotionView) parent.getAdapter().getView(position, null, null);
            Drawable d = ev.getmDrawable();
            d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
            text.setSpan(new ImageSpan(d), index, index + emoticon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class EmoticonPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
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
            switch (position) {
                case 0:
                    gridView.setAdapter(new EmoticonGridAdapter());
                    gridView.setOnItemClickListener(new AcEmoticonGridItemClickListener());
                    break;
                case 1:
                    gridView.setAdapter(null);
                    break;
            }
            container.addView(gridView);
            return container.getChildAt(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class EmoticonGridAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new EmotionView(getApplicationContext());
            }
            ((EmotionView) convertView).setEmotionId(position + 1);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            String cat;
            int id;
            if (position < 54) {
                cat = "ac";
                id = position + 1;
            } else {
                cat = position >= 94 ? "ac2" : "ac";
                id = position >= 94 ? position - 93 : position - 53;
            }
            return String.format(Locale.US, "[emot=%s,%02d/]", cat, id);
        }

        @Override
        public int getCount() {
            return 149;
        }
    }
}
