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

public class ConfirmDeleteCommentDialogFragment extends AppCompatDialogFragment {

    private static final String KEY_PREFIX = ConfirmDeleteCommentDialogFragment.class.getName()
            + '.';

    public static final String EXTRA_COMMENT = KEY_PREFIX + "comment";

    private Comment mComment;

    /**
     * @deprecated Use {@link #newInstance(Comment)} instead.
     */
    public ConfirmDeleteCommentDialogFragment() {}

    public static ConfirmDeleteCommentDialogFragment newInstance(Comment comment) {
        //noinspection deprecation
        ConfirmDeleteCommentDialogFragment fragment = new ConfirmDeleteCommentDialogFragment();
        FragmentUtils.ensureArguments(fragment)
                .putParcelable(EXTRA_COMMENT, comment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComment = getArguments().getParcelable(EXTRA_COMMENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(mika.com.android.ac.R.string.broadcast_comment_delete_confirm)
                .setPositiveButton(mika.com.android.ac.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getListener().deleteComment(mComment);
                    }
                })
                .setNegativeButton(mika.com.android.ac.R.string.cancel, null)
                .create();
    }

    private Listener getListener() {
        return (Listener) getParentFragment();
    }

    public static void show(Comment comment, Fragment fragment) {
        ConfirmDeleteCommentDialogFragment.newInstance(comment)
                .show(fragment.getChildFragmentManager(), null);
    }

    public interface Listener {
        void deleteComment(Comment comment);
    }
}
