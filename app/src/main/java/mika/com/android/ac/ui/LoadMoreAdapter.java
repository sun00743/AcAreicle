/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
*
*/

package mika.com.android.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.util.ViewUtils;

public class LoadMoreAdapter extends MergeAdapter {

    private LoadMoreViewAdapter mViewAdapter;

    /**
     * @param loadMoreLayoutRes 加载更多时显示的界面，比如一个progress
     * @param adapters          RecycleView的adapter
     */
    public LoadMoreAdapter(int loadMoreLayoutRes, RecyclerView.Adapter<?>... adapters) {
        super(mergeAdapters(adapters, new LoadMoreViewAdapter(loadMoreLayoutRes)));

        adapters = getAdapters();
        mViewAdapter = (LoadMoreViewAdapter) adapters[adapters.length - 1];
        mViewAdapter.setParentAdapter(this);
    }

    private static RecyclerView.Adapter<?>[] mergeAdapters(RecyclerView.Adapter<?>[] adapters,
                                                           RecyclerView.Adapter<?> adapter) {
        RecyclerView.Adapter<?>[] mergedAdapters = new RecyclerView.Adapter<?>[adapters.length + 1];
        System.arraycopy(adapters, 0, mergedAdapters, 0, adapters.length);
        mergedAdapters[adapters.length] = adapter;
        return mergedAdapters;
    }

    public boolean isProgressVisible() {
        return mViewAdapter.isProgressVisible();
    }

    public void setProgressVisible(boolean progressVisible) {
        mViewAdapter.setProgressVisible(progressVisible);
    }

    public void setItemVisible(boolean visible) {
        mViewAdapter.setItemViewVisible(visible);
    }

    public void setItemText(String text){
        mViewAdapter.setTextString(text);
    }

    public void setNoMoreYet(boolean noMoreYet) {
        mViewAdapter.setNoMoreYet(noMoreYet);
    }

    public void onLoadStarted() {
        mViewAdapter.setTextString(null);
        mViewAdapter.setProgressVisible(true);
    }

    static class LoadMoreViewAdapter extends RecyclerView.Adapter<LoadMoreViewAdapter.ViewHolder> {

        private int mLoadMoreLayoutRes;
        /**
         * 若无数据，则不展示load more item
         */
        private boolean mShowingItem;
        private ViewHolder mViewHolder;
        /**
         * load more item visible or invisible
         */
        private boolean mItemViewVisible = true;
        /**
         * 判断没有更多还是无数据
         */
        private boolean isNoMoreYet;
        /**
         * progress visible or text visible
         */
        private boolean mProgressVisible;
        private String mText;

        public LoadMoreViewAdapter(int loadMoreLayoutResId) {

            mLoadMoreLayoutRes = loadMoreLayoutResId;

            setHasStableIds(true);
        }

        // We need to postpone this until the super call of our parent is completed.
        public void setParentAdapter(final LoadMoreAdapter parentAdapter) {
            setShowingItem(parentAdapter.getItemCount() > 0);
            parentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    onChanged();
                }

                @Override
                public void onChanged() {
                    // Don't show the progress item if our parent is empty - or else this can lead
                    // to incorrect scrolling position when items are added (before us).
                    setShowingItem(parentAdapter.getItemCount() > 0);
                }
            });
        }

        public void setShowingItem(boolean showingItem) {

            if (mShowingItem == showingItem) {
                return;
            }

            mShowingItem = showingItem;
            if (mShowingItem) {
                notifyItemInserted(0);
            } else {
                notifyItemRemoved(0);
            }
        }

        public boolean isProgressVisible() {
            return mProgressVisible;
        }

        public void setProgressVisible(boolean progressVisible) {

            if (mProgressVisible == progressVisible) {
                return;
            }

            mProgressVisible = progressVisible;
            if (mShowingItem) {
                if (mViewHolder != null) {
                    onBindViewHolder(mViewHolder, 0);
                } else {
                    notifyItemChanged(0);
                }
            }
        }

        public void setNoMoreYet(boolean noMoreYet) {
            if (isNoMoreYet == noMoreYet) {
                return;
            }
            isNoMoreYet = noMoreYet;
        }

        public void setItemViewVisible(boolean visible) {
            if (mItemViewVisible == visible) {
                return;
            }
            mItemViewVisible = visible;
            if (mShowingItem) {
                if (mViewHolder != null) {
                    notifyItemChanged(0);
                }
            }
        }

        public void setTextString(String text) {
            if (text == null || text.isEmpty()) {
                mText = null;
                return;
            }
            mText = text;
            if (mShowingItem) {
                if (mViewHolder != null) {
                    onBindViewHolder(mViewHolder, 0);
                } else {
                    notifyItemChanged(0);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mShowingItem ? 1 : 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(ViewUtils.inflate(mLoadMoreLayoutRes, parent));
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutParams =
                        (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                staggeredGridLayoutParams.setFullSpan(true);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            ViewUtils.setVisibleOrInvisible(holder.itemView, mItemViewVisible);
            ViewUtils.setVisibleOrInvisible(holder.progress, mProgressVisible && mItemViewVisible);
            ViewUtils.setVisibleOrInvisible(holder.textView, !mProgressVisible && mItemViewVisible);
            if (isNoMoreYet) {
                holder.textView.setText(R.string.load_more_no_more);
            } else {
                holder.textView.setText(R.string.load_more_nothing);
            }
            if (mText != null && !mText.isEmpty()) {
                holder.textView.setText(mText);
            }
            mViewHolder = holder;
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            mViewHolder = null;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.progress)
            public ProgressBar progress;
            @BindView(R.id.no_more)
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }
        }
    }
}
