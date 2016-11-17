/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.util.FragmentUtils;

/**
 * Simple dialog for comment action. Requires the host fragment to implement {@link Listener}.
 */
public class CommentActionDialogFragment extends AppCompatDialogFragment {

    private static final String KEY_PREFIX = CommentActionDialogFragment.class.getName() + '.';

    public static final String EXTRA_COMMENT = KEY_PREFIX + "comment";
    public static final String EXTRA_CAN_REPLY_TO = KEY_PREFIX + "can_reply_to";
    public static final String EXTRA_CAN_DELETE = KEY_PREFIX + "can_delete";

    private Comment mComment;
    private boolean mCanReplyTo;
    private boolean mCanDelete;

    /**
     * @deprecated Use {@link #newInstance(Comment, boolean, boolean)} instead.
     */
    public CommentActionDialogFragment() {}

    public static CommentActionDialogFragment newInstance(Comment comment, boolean canReplyTo,
                                                          boolean canDelete) {
        //noinspection deprecation
        CommentActionDialogFragment fragment = new CommentActionDialogFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putParcelable(EXTRA_COMMENT, comment);
        arguments.putBoolean(EXTRA_CAN_REPLY_TO, canReplyTo);
        arguments.putBoolean(EXTRA_CAN_DELETE, canDelete);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mComment = arguments.getParcelable(EXTRA_COMMENT);
        mCanReplyTo = arguments.getBoolean(EXTRA_CAN_REPLY_TO);
        mCanDelete = arguments.getBoolean(EXTRA_CAN_DELETE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CharSequence[] items;
        DialogInterface.OnClickListener onClickListener;
        if (mCanReplyTo && mCanDelete) {
            items = new CharSequence[]{
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_reply_to),
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_copy_text),
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_delete)
            };
            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            onReplyToComment();
                            break;
                        case 1:
                            onCopyCommentText();
                            break;
                        case 2:
                            onDeleteComment();
                            break;
                    }
                }
            };
        } else if (mCanReplyTo) {
            items = new CharSequence[]{
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_reply_to),
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_copy_text)
            };
            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            onReplyToComment();
                            break;
                        case 1:
                            onCopyCommentText();
                            break;
                    }
                }
            };
        } else if (mCanDelete) {
            items = new CharSequence[]{
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_copy_text),
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_delete)
            };
            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            onCopyCommentText();
                            break;
                        case 1:
                            onDeleteComment();
                            break;
                    }
                }
            };
        } else {
            items = new CharSequence[]{
                    getString(mika.com.android.ac.R.string.broadcast_comment_action_copy_text),
            };
            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            onCopyCommentText();
                            break;
                    }
                }
            };
        }

        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(mika.com.android.ac.R.string.broadcast_comment_action_title)
                .setItems(items, onClickListener)
                .create();
    }

    private void onReplyToComment() {
        getListener().onReplyToComment(mComment);
    }

    private void onCopyCommentText() {
        getListener().onCopyCommentText(mComment);
    }

    private void onDeleteComment() {
        getListener().onDeleteComment(mComment);
    }

    private Listener getListener() {
        return (Listener) getParentFragment();
    }

    public static void show(Comment comment, boolean canReplyTo, boolean canDelete,
                            Fragment fragment) {
        CommentActionDialogFragment.newInstance(comment, canReplyTo, canDelete)
                .show(fragment.getChildFragmentManager(), null);
    }

    public interface Listener {
        void onReplyToComment(Comment comment);
        void onCopyCommentText(Comment comment);
        void onDeleteComment(Comment comment);
    }
}
