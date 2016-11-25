/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.ui.ClickableSimpleAdapter;
import mika.com.android.ac.ui.TimeTextView;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.RecyclerViewUtils;
import mika.com.android.ac.util.ViewUtils;

public class CommentAdapter extends ClickableSimpleAdapter<Comment, CommentAdapter.ViewHolder> {

    public CommentAdapter(List<Comment> commentList,
                          OnItemClickListener<Comment, CommentAdapter.ViewHolder> onItemClickListener) {
        super(commentList, onItemClickListener, null);

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(mika.com.android.ac.R.layout.broadcast_comment_item,
                parent));
        ViewUtils.setTextViewLinkClickable(holder.textText);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment comment = getItem(position);
        ImageUtils.loadAvatar(holder.avatarImage, comment.author.avatar);
        final Context context = RecyclerViewUtils.getContext(holder);
        holder.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(ProfileActivity.makeIntent(comment.author, context));
            }
        });
        holder.nameText.setText(comment.author.name);
        holder.timeText.setDoubanTime(comment.createdAt);
        holder.textText.setText(comment.getContentWithEntities(context));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatarImage.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(mika.com.android.ac.R.id.avatar)
        public ImageView avatarImage;
        @BindView(mika.com.android.ac.R.id.name)
        public TextView nameText;
        @BindView(mika.com.android.ac.R.id.time)
        public TimeTextView timeText;
        @BindView(mika.com.android.ac.R.id.text)
        public TextView textText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
