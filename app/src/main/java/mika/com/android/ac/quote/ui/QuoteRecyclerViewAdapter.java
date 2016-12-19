package mika.com.android.ac.quote.ui;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.TextViewUtils;

/**
 * Created by mika on 2016/12/14.
 */

public class QuoteRecyclerViewAdapter extends RecyclerView.Adapter<QuoteRecyclerViewAdapter.QuoteHolder> {
    private Context context;
    private List<Comment> mQuoteList = new ArrayList<>();
    private SparseArray<Comment> mMyL1ist = new SparseArray<>();

    public QuoteRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public QuoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item, parent, false));
    }

    @Override
    public void onBindViewHolder(QuoteHolder holder, int position) {
        if (isEmpty())
            return;

        Comment qComment = mQuoteList.get(position);

        if (qComment == null)
            return;

        Comment mComment = mMyL1ist.get(qComment.quoteId);

        if (qComment.title == null) {
            holder.title.setText(R.string.quote_title_delete);
        } else {
            holder.title.setText(qComment.title);
        }
        if (qComment.avatar != null) {
            ImageUtils.loadAvatar(holder.qAvatar, qComment.avatar);
        }
        holder.qName.setText(qComment.username);
        TextViewUtils.setCommentContent(holder.qContent, qComment);

        if (mComment == null || mComment.avatar == null) {
            holder.mName.setText(R.string.quote_acer_delete);
            holder.mContent.setText(R.string.quote_comment_delete);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mAvatar.setImageDrawable(context.getDrawable(R.drawable.avatar_icon_grey600_40dp));
            } else {
                holder.mAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_icon_grey600_40dp));
            }
        } else {
//            if (mComment.avatar != null) {
            ImageUtils.loadAvatar(holder.mAvatar, mComment.avatar);
//            }
            holder.mName.setText(mComment.username);
            TextViewUtils.setCommentContent(holder.mContent, mComment);
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuoteList.size();
    }

    public void replace(List<Comment> qList, SparseArray<Comment> mList) {
        mQuoteList = qList;
        mMyL1ist = mList;
        notifyDataSetChanged();
    }

    public void append(List<Comment> qList, List<Comment> mList) {
        for (int i = 0; i < mList.size(); i++) {
            mMyL1ist.append(mList.get(i).id, mList.get(i));
        }
        mQuoteList.addAll(qList);
        notifyItemRangeInserted(mQuoteList.size() - 1, qList.size());
    }

    public boolean isEmpty() {
        return getItemCount() <= 0 || mQuoteList == null || mMyL1ist == null;
    }

    class QuoteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quote_article_name)
        TextView title;
        @BindView(R.id.quote_avatar_q)
        ImageView qAvatar;
        @BindView(R.id.quote_name_q)
        TextView qName;
        @BindView(R.id.quote_content_q)
        TextView qContent;
        @BindView(R.id.quote_avatar_my)
        ImageView mAvatar;
        @BindView(R.id.quote_name_my)
        TextView mName;
        @BindView(R.id.quote_content_my)
        TextView mContent;

        public QuoteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
