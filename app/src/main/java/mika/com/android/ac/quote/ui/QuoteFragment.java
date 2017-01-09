package mika.com.android.ac.quote.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.quote.content.QuoteListResource;
import mika.com.android.ac.ui.FriendlySwipeRefreshLayout;
import mika.com.android.ac.ui.LoadMoreAdapter;
import mika.com.android.ac.ui.NoChangeAnimationItemAnimator;
import mika.com.android.ac.ui.OnVerticalScrollWithPagingTouchSlopListener;

/**
 * Created by mika <sun00743@gmail.com> 2016-12-13
 * <p>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuoteFragment.OnQuoteInteractionListener} interface
 * to handle interaction events.
 */
public class QuoteFragment extends Fragment implements
        QuoteListResource.OnListChangeListener {

    @BindDimen(R.dimen.toolbar_height)
    int toolbarHeight;
    @BindView(R.id.frag_quote_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.quote_refresh)
    FriendlySwipeRefreshLayout refreshLayout;
    @BindView(R.id.quote_recycler)
    RecyclerView recyclerView;

    private QuoteListResource mResource;
    private OnQuoteInteractionListener mListener;
    private QuoteRecyclerViewAdapter mRecyclerAdapter;
    private LoadMoreAdapter mLoadMoreAdapter;

    public QuoteFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuoteInteractionListener) {
            mListener = (OnQuoteInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnQuoteInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quote, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init resource
        mResource = new QuoteListResource(getContext(), this);
        mResource.loadFromCache();
        //config refresh layout
        refreshLayout.setProgressViewOffset(toolbarHeight);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mResource.load(false);
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        initRecycler();
    }

    /**
     * settings recyclerView
     */
    private void initRecycler() {
        mRecyclerAdapter = new QuoteRecyclerViewAdapter(getContext());
        mLoadMoreAdapter = new LoadMoreAdapter(R.layout.load_more_card_item, mRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new NoChangeAnimationItemAnimator());
        recyclerView.setAdapter(mLoadMoreAdapter);
        // add scrollListener
        recyclerView.addOnScrollListener(new OnVerticalScrollWithPagingTouchSlopListener(getContext()) {
            @Override
            public void onScrolledToBottom() {
                super.onScrolledToBottom();
                mResource.load(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!mRecyclerAdapter.isEmpty()) {
            mResource.saveQuoteComment();
        }
        mListener = null;
    }

    private void updateRefresh() {
        boolean loadMore = mResource.loadMore();
        boolean empty = mResource.isEmpty();
        boolean loading = mResource.isLoading();
        boolean canLoadMore = mResource.canLoadMore();
        refreshLayout.setRefreshing(loading && !loadMore);
        mLoadMoreAdapter.setItemVisible(!canLoadMore || empty || (loadMore && loading));
        mLoadMoreAdapter.setNoMoreYet(!empty);
        mLoadMoreAdapter.setProgressVisible(loadMore);
    }

    @Override
    public void onListChanged(List<Comment> qComment, SparseArray<Comment> MyComment) {
        mRecyclerAdapter.replace(qComment, MyComment);
    }

    @Override
    public void onListAppend(List<Comment> qComment, List<Comment> MyComment) {
        mRecyclerAdapter.append(qComment, MyComment);
    }

    @Override
    public void onListLoadFinished() {
        refreshLayout.setRefreshing(false);
        mLoadMoreAdapter.setItemVisible(!mResource.canLoadMore());
        mLoadMoreAdapter.setNoMoreYet(!mResource.isEmpty());
        mLoadMoreAdapter.setProgressVisible(false);
    }

    @Override
    public void onListLoadStarted() {
        refreshLayout.setRefreshing(!mResource.isLoading() && !mResource.loadMore());
        if (mResource.loadMore()) {
            mLoadMoreAdapter.setItemVisible(true);
            mLoadMoreAdapter.onLoadStarted();
        }
    }

    @Override
    public void onListLoadError(VolleyError error) {
        if (this.isDetached())
            return;
        mLoadMoreAdapter.setItemText(getString(R.string.load_more_failed));
    }

    public interface OnQuoteInteractionListener {

    }
}
