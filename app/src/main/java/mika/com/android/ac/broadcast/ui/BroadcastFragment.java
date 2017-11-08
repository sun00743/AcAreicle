/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import mika.com.android.ac.R;
import mika.com.android.ac.broadcast.content.BroadcastAndCommentListResource;
import mika.com.android.ac.broadcast.content.BroadcastCommentCountFixer;
import mika.com.android.ac.broadcast.content.DeleteBroadcastCommentManager;
import mika.com.android.ac.broadcast.content.DeleteBroadcastManager;
import mika.com.android.ac.broadcast.content.LikeBroadcastManager;
import mika.com.android.ac.broadcast.content.RebroadcastBroadcastManager;
import mika.com.android.ac.broadcast.content.SendBroadcastCommentManager;
import mika.com.android.ac.eventbus.BroadcastCommentSendErrorEvent;
import mika.com.android.ac.eventbus.BroadcastCommentSentEvent;
import mika.com.android.ac.eventbus.EventBusUtils;
import mika.com.android.ac.network.api.ApiError;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.ui.ClickableSimpleAdapter;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.ui.OnVerticalScrollListener;
import mika.com.android.ac.util.CheatSheetUtils;
import mika.com.android.ac.util.ClipboardUtils;
import mika.com.android.ac.util.DoubanUtils;
import mika.com.android.ac.util.FragmentUtils;
import mika.com.android.ac.util.ImeUtils;
import mika.com.android.ac.util.LogUtils;
import mika.com.android.ac.util.ToastUtil;
import mika.com.android.ac.util.TransitionUtils;
import mika.com.android.ac.util.ViewUtils;

public class BroadcastFragment extends Fragment implements BroadcastAndCommentListResource.Listener,
        SingleBroadcastAdapter.Listener, CommentActionDialogFragment.Listener,
        ConfirmDeleteCommentDialogFragment.Listener, ConfirmDeleteBroadcastDialogFragment.Listener {

    private static final String KEY_PREFIX = BroadcastFragment.class.getName() + '.';

    private static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";
    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_SHOW_SEND_COMMENT = KEY_PREFIX + "show_send_comment";
    private static final String EXTRA_TITLE = KEY_PREFIX + "title";

    @BindView(mika.com.android.ac.R.id.container)
    FrameLayout mContainerLayout;
    @BindView(mika.com.android.ac.R.id.toolbar)
    Toolbar mToolbar;
    @BindView(mika.com.android.ac.R.id.shared)
    View mSharedView;
    @BindView(mika.com.android.ac.R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(mika.com.android.ac.R.id.broadcast_comment_list)
    RecyclerView mBroadcastCommentList;
    @BindView(mika.com.android.ac.R.id.progress)
    ProgressBar mProgress;
    @BindView(mika.com.android.ac.R.id.comment)
    EditText mCommentEdit;
    @BindView(mika.com.android.ac.R.id.send)
    ImageButton mSendButton;

    private Menu mMenu;

    private long mBroadcastId;
    private Broadcast mBroadcast;
    private boolean mShowSendComment;
    private String mTitle;

    private BroadcastAndCommentListResource mBroadcastAndCommentListResource;

    private SingleBroadcastAdapter mBroadcastAdapter;
    private CommentAdapter mCommentAdapter;
    private LoadMoreAdapter mAdapter;

    /**
     * 是否只有一个条目
     */
    private boolean unSingle = false;

    public static BroadcastFragment newInstance(long broadcastId, Broadcast broadcast,
                                                boolean showSendComment, String title) {
        //noinspection deprecation
        BroadcastFragment fragment = new BroadcastFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putLong(EXTRA_BROADCAST_ID, broadcastId);
        arguments.putParcelable(EXTRA_BROADCAST, broadcast);
        arguments.putBoolean(EXTRA_SHOW_SEND_COMMENT, showSendComment);
        arguments.putString(EXTRA_TITLE, title);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(long, Broadcast, boolean, String)} instead.
     */
    public BroadcastFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mBroadcast = arguments.getParcelable(EXTRA_BROADCAST);
        if (mBroadcast != null) {
            mBroadcastId = mBroadcast.id;
        } else {
            mBroadcastId = arguments.getLong(EXTRA_BROADCAST_ID);
        }
        mShowSendComment = arguments.getBoolean(EXTRA_SHOW_SEND_COMMENT);
        mTitle = arguments.getString(EXTRA_TITLE);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.broadcast_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomTabsHelperFragment.attachTo(this);
        mBroadcastAndCommentListResource = BroadcastAndCommentListResource.attachTo(mBroadcastId,
                mBroadcast, this);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(getTitle());
        activity.setSupportActionBar(mToolbar);

        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(activity);
            }
        });
        ViewCompat.setTransitionName(mSharedView, Broadcast.makeTransitionName(mBroadcastId));
        // This magically gives better visual effect when the broadcast is partially visible. Using
        // setEnterSharedElementCallback() disables this hack when no transition is used to start
        // this Activity.
        ActivityCompat.setEnterSharedElementCallback(activity, new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {
                mBroadcastCommentList.scrollToPosition(0);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBroadcastAndCommentListResource.loadBroadcast();
                mBroadcastAndCommentListResource.loadCommentList(false);
            }
        });

        mBroadcastCommentList.setHasFixedSize(true);
        mBroadcastCommentList.setItemAnimator(new NoChangeAnimationItemAnimator());
        mBroadcastCommentList.setLayoutManager(new LinearLayoutManager(activity));

        mBroadcastAdapter = new SingleBroadcastAdapter(null, this);
        setBroadcast(mBroadcastAndCommentListResource.getBroadcast());

        mCommentAdapter = new CommentAdapter(mBroadcastAndCommentListResource.getCommentList(),
                new ClickableSimpleAdapter.OnItemClickListener<Comment,
                                        CommentAdapter.ViewHolder>() {
                    @Override
                    public void onItemClick(RecyclerView parent, Comment item,
                                            CommentAdapter.ViewHolder holder) {
                        onShowCommentAction(item);
                    }
                });
        //初始化adapter
        mAdapter = new LoadMoreAdapter(mika.com.android.ac.R.layout.load_more_item, mBroadcastAdapter, mCommentAdapter);
        mBroadcastCommentList.setAdapter(mAdapter);

        mBroadcastCommentList.addOnScrollListener(new OnVerticalScrollListener() {
            public void onScrolledToBottom() {
                mBroadcastAndCommentListResource.loadCommentList(true);
            }
        });

        CheatSheetUtils.setup(mSendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendComment();
            }
        });
        updateSendCommentStatus();

        if (savedInstanceState == null) {
            if (mShowSendComment) {
                TransitionUtils.postAfterTransition(this, new Runnable() {
                    @Override
                    public void run() {
                        onShowSendComment();
                    }
                });
            }
        }

        TransitionUtils.setEnterReturnExplode(this);
        TransitionUtils.setupTransitionOnActivityCreated(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBroadcastAndCommentListResource.detach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(mika.com.android.ac.R.menu.broadcast, menu);
        mMenu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        updateOptionsMenu();
    }

    private void updateOptionsMenu() {
        if (mMenu == null) {
            return;
        }
        Broadcast broadcast = mBroadcastAndCommentListResource.getBroadcast();
        boolean hasBroadcast = broadcast != null;
        mMenu.findItem(mika.com.android.ac.R.id.action_copy_text).setVisible(hasBroadcast);
//        boolean canDelete = hasBroadcast && broadcast.isAuthorOneself(getActivity());
//        mMenu.findItem(R.id.action_delete).setVisible(canDelete);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(getActivity());
                return true;
            case mika.com.android.ac.R.id.action_copy_text:
                copyText();
                return true;
//            case mika.com.android.ac.R.id.action_delete:
//                onDeleteBroadcast();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getTitle() {
        return !TextUtils.isEmpty(mTitle) ? mTitle : getString(mika.com.android.ac.R.string.broadcast_title);
    }

    @Override
    public void onLoadBroadcastStarted(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadBroadcastFinished(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadBroadcastError(int requestCode, VolleyError error) {
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onBroadcastChanged(int requestCode, Broadcast newBroadcast) {
        setBroadcast(newBroadcast);
    }

    @Override
    public void onBroadcastRemoved(int requestCode) {
        getActivity().finish();
    }

    @Override
    public void onBroadcastWriteStarted(int requestCode) {
        mBroadcastAdapter.notifyBroadcastChanged();
    }

    @Override
    public void onBroadcastWriteFinished(int requestCode) {
        mBroadcastAdapter.notifyBroadcastChanged();
    }

    private void setBroadcast(Broadcast broadcast) {
        mBroadcastAdapter.setBroadcast(broadcast);
        updateOptionsMenu();
        updateSendCommentStatus();
    }

    @Override
    public void onLoadCommentListStarted(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadCommentListFinished(int requestCode) {
        updateRefreshing();
    }

    @Override
    public void onLoadCommentListError(int requestCode, VolleyError error) {
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtil.show(ApiError.getErrorString(error, activity), activity);
    }

    @Override
    public void onCommentListChanged(int requestCode, List<Comment> newCommentList) {
        mCommentAdapter.replace(newCommentList);
        BroadcastCommentCountFixer.onCommentListChanged(
                mBroadcastAndCommentListResource.getBroadcast(),
                mBroadcastAndCommentListResource.getCommentList(), this);
    }

    @Override
    public void onCommentListAppended(int requestCode, List<Comment> appendedCommentList) {
        mCommentAdapter.addAll(appendedCommentList);
        BroadcastCommentCountFixer.onCommentListChanged(
                mBroadcastAndCommentListResource.getBroadcast(),
                mBroadcastAndCommentListResource.getCommentList(), this);
    }

    @Override
    public void onCommentRemoved(int requestCode, int position) {
        mCommentAdapter.remove(position);
        BroadcastCommentCountFixer.onCommentRemoved(
                mBroadcastAndCommentListResource.getBroadcast(), this);
    }

    private void updateRefreshing() {
        boolean loadingBroadcast = mBroadcastAndCommentListResource.isLoadingBroadcast();
        boolean hasBroadcast = mBroadcastAndCommentListResource.hasBroadcast();
        boolean loadingCommentList = mBroadcastAndCommentListResource.isLoadingCommentList();
        mSwipeRefreshLayout.setRefreshing(loadingBroadcast
                && (mSwipeRefreshLayout.isRefreshing() || hasBroadcast));
        ViewUtils.setVisibleOrGone(mProgress, loadingBroadcast && !hasBroadcast);
        mAdapter.setProgressVisible((unSingle) && hasBroadcast && loadingCommentList);
    }

    @Override
    public void onLike(Broadcast broadcast, boolean like) {
        LikeBroadcastManager.getInstance().write(broadcast, like, getActivity());
    }

    @Override
    public void onRebroadcast(Broadcast broadcast, boolean rebroadcast) {
        RebroadcastBroadcastManager.getInstance().write(broadcast, rebroadcast, getActivity());
    }

    @Override
    public void onComment(Broadcast broadcast) {
        onShowSendComment();
    }

    @Override
    public void onViewActivity(Broadcast broadcast) {
        BroadcastActivityDialogFragment.show(broadcast, this);
    }

    private void onShowCommentAction(Comment comment) {
        boolean canReplyTo = canSendComment();
        Activity activity = getActivity();
        boolean canDelete = (mBroadcastAdapter.hasBroadcast()
                && mBroadcastAdapter.getBroadcast().isAuthorOneself(activity))
                || comment.isAuthorOneself(activity);
        CommentActionDialogFragment.show(comment, canReplyTo, canDelete, this);
    }

    @Override
    public void onReplyToComment(Comment comment) {
        mCommentEdit.getText().replace(mCommentEdit.getSelectionStart(),
                mCommentEdit.getSelectionEnd(), DoubanUtils.getAtUserString(comment.author));
        onShowSendComment();
    }

    @Override
    public void onCopyCommentText(Comment comment) {
        Activity activity = getActivity();
        ClipboardUtils.copyText(comment.getClipboardLabel(), comment.getClipboardText(activity),
                activity);
    }

    @Override
    public void onDeleteComment(Comment comment) {
        ConfirmDeleteCommentDialogFragment.show(comment, this);
    }

    @Override
    public void deleteComment(Comment comment) {
        DeleteBroadcastCommentManager.getInstance().write(
                mBroadcastAndCommentListResource.getBroadcastId(), comment.id, getActivity());
    }

    private void onShowSendComment() {
        if (canSendComment()) {
            ImeUtils.showIme(mCommentEdit);
        } else {
            ToastUtil.show(mika.com.android.ac.R.string.broadcast_send_comment_disabled, getActivity());
        }
    }

    private void onSendComment() {

        String comment = mCommentEdit.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            ToastUtil.show(mika.com.android.ac.R.string.broadcast_send_comment_error_empty, getActivity());
            return;
        }

        sendComment(comment);
    }

    private void sendComment(String comment) {

        SendBroadcastCommentManager.getInstance().write(
                mBroadcastAndCommentListResource.getBroadcastId(), comment, getActivity());

        updateSendCommentStatus();
    }

    @Keep
    public void onEventMainThread(BroadcastCommentSentEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastAndCommentListResource.getBroadcastId()) {
            mBroadcastCommentList.scrollToPosition(mAdapter.getItemCount() - 1);
            mCommentEdit.setText(null);
            updateSendCommentStatus();
        }
    }

    @Keep
    public void onEventMainThread(BroadcastCommentSendErrorEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastAndCommentListResource.getBroadcastId()) {
            updateSendCommentStatus();
        }
    }

    private boolean canSendComment() {
        Broadcast broadcast = mBroadcastAndCommentListResource.getBroadcast();
        return broadcast != null && broadcast.canComment();
    }

    private void updateSendCommentStatus() {
        boolean canSendComment = canSendComment();
        SendBroadcastCommentManager manager = SendBroadcastCommentManager.getInstance();
        long broadcastId = mBroadcastAndCommentListResource.getBroadcastId();
        boolean sendingComment = manager.isWriting(broadcastId);
        boolean enabled = canSendComment && !sendingComment;
        mCommentEdit.setEnabled(enabled);
        mSendButton.setEnabled(enabled);
        boolean hasBroadcast = mBroadcastAndCommentListResource.hasBroadcast();
        mCommentEdit.setHint(!hasBroadcast || canSendComment ? mika.com.android.ac.R.string.broadcast_send_comment_hint
                : mika.com.android.ac.R.string.broadcast_send_comment_hint_disabled);
        if (sendingComment) {
            mCommentEdit.setText(manager.getComment(broadcastId));
        }
    }

    private void copyText() {

        Broadcast broadcast = mBroadcastAdapter.getBroadcast();
        Activity activity = getActivity();
        if (broadcast == null) {
            ToastUtil.show(mika.com.android.ac.R.string.broadcast_copy_text_not_loaded, activity);
            return;
        }

        ClipboardUtils.copyText(broadcast.getClipboradLabel(), broadcast.getClipboardText(activity),
                activity);
    }

    private void onDeleteBroadcast() {
        ConfirmDeleteBroadcastDialogFragment.show(this);
    }

    @Override
    public void deleteBroadcast() {
        DeleteBroadcastManager.getInstance().write(
                mBroadcastAndCommentListResource.getBroadcastId(), getActivity());
    }
}
