/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.articlelist.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.acapi.ArticleList;
import mika.com.android.ac.ui.SimpleAdapter;
import mika.com.android.ac.util.ViewUtils;

class ArticleDesAdapter extends SimpleAdapter<ArticleList, ArticleDesAdapter.ViewHolder> {

    private OnBtnClickedListener mOnBtnClickedListener;
    private int lastPosition;

    ArticleDesAdapter(List<ArticleList> articleLists, OnBtnClickedListener listener){
        super(articleLists);
        this.mOnBtnClickedListener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(mika.com.android.ac.R.layout.articledes_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Log.i("ssssssss: ", getItem(position).title + "" + holder.itemView.isShown());
//        while (holder.itemView.isShown() == false){
//            //加载头像
//        }

//        ArticleList originalArticleList = getItem(position);
        final ArticleList articleList = getItem(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBtnClickedListener.onOpenArticle(articleList, getSharedView(holder));
            }
        });

//        if(!holder.itemView.isShown()){
//            holder.articleDesLayout.loadAvatarImage(articleList);
//        }
        holder.articleDesLayout.isInList(true);
        holder.articleDesLayout.bindArticleList(articleList);
        holder.articleDesLayout.setOnBtnClickedListener(new ArticleListLayout.OnBtnClickedListener()  {
            @Override
            public void onStarClicked() {
                mOnBtnClickedListener.onStartClicked(articleList, false);
            }

            @Override
            public void onBananaClicked() {
                mOnBtnClickedListener.onBananaClicked(articleList, false);
            }

            @Override
            public void onCommentClicked() {
                mOnBtnClickedListener.onCommentClicked(articleList, getSharedView(holder));
            }

            @Override
            public void onInListClicked() {
                mOnBtnClickedListener.onOpenArticle(articleList, getSharedView(holder));
            }
        });
        ViewCompat.setTransitionName(getSharedView(holder), articleList.makeTransitionName());


    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
//        Log.i("rrrrrrrr: ", getItem(lastPosition).title + "" + holder.itemView.isShown());
        holder.articleDesLayout.releaseArticleList();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
//        holder.articleDesLayout.cleanAvatarImage();
        super.onViewDetachedFromWindow(holder);
    }

    private static View getSharedView(ViewHolder holder){
        Context context = holder.itemView.getContext();
        return ViewUtils.hasSw600Dp(context) ? holder.itemView : holder.articleDesLayout;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(mika.com.android.ac.R.id.card)
        CardView cardView;
        @BindView(mika.com.android.ac.R.id.articledes)
        ArticleListLayout articleDesLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    interface OnBtnClickedListener {
        void onStartClicked(ArticleList articleList, boolean start);
        void onBananaClicked(ArticleList articleList, boolean Bananaed);

        /**
         * 评论按钮
         *
         */
        void onCommentClicked(ArticleList articleList, View sharedView);

        /**
         * 打开文章
         *
         */
        void onOpenArticle(ArticleList articleList, View sharedView);
    }
}
