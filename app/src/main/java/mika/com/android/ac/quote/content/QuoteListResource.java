package mika.com.android.ac.quote.content;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.QuoteRequest;
import mika.com.android.ac.network.api.info.acapi.Comment;
import mika.com.android.ac.network.api.info.acapi.QuoteResult;
import mika.com.android.ac.util.Callback;

/**
 * Created by mika on 2016/12/14.
 */

public class QuoteListResource {

    private Context context;
    private int usersId;
    private Handler mHandler = new Handler();
    private boolean loadMore;
    private boolean canLoadMore = true;
    private boolean isLoading;
    private OnListChangeListener mListChangeListener;
    private int pageNO = 1;
    private static final int PAGE_SIZE = 10;
    private List<Comment> qList = new ArrayList<>();
    private List<Comment> mList = new ArrayList<>();

    public QuoteListResource(Context context, OnListChangeListener listener) {
        this.context = context;
        this.mListChangeListener = listener;
    }

    public void loadFromCache() {
        if (isLoading) {
            return;
        }
        mListChangeListener.onListLoadStarted();
        isLoading = true;

        QuoteListCache.getQuoteQComment(usersId, mHandler, new Callback<List<Comment>>() {
            @Override
            public void onValue(List<Comment> qComment) {
                //if hasn't cache return and load from server
                if (qComment == null || qComment.size() <= 0) {
                    isLoading = false;
                    load(false);
                    return;
                }

                qList = qComment;
                //不知道哪个先执行完，所以每次读取完毕后判断都有数据后再操作
                if (qList.size() > 0 && mList.size() > 0) {
                    onLoadFromCacheComplete(qList, mList);
                }
            }
        }, context);
        QuoteListCache.getQUoteMyComment(usersId, mHandler, new Callback<List<Comment>>() {
            @Override
            public void onValue(List<Comment> value) {
                //if hasn't cache return and load from server
                if (value == null || value.size() <= 0) {
                    isLoading = false;
                    load(false);
                    return;
                }

                mList = value;
                if (qList.size() > 0 && mList.size() > 0) {
                    onLoadFromCacheComplete(qList, mList);
                }
            }
        }, context);
    }

    private void onLoadFromCacheComplete(List<Comment> qComment, List<Comment> myComment) {
        isLoading = false;

//        SparseArray<Comment> qArray = new SparseArray<>();
        SparseArray<Comment> mArray = new SparseArray<>();
        for (int i = 0; i < myComment.size(); i++) {
            mArray.put(myComment.get(i).id, myComment.get(i));
        }
        mListChangeListener.onListChanged(qComment, mArray);
        load(false);
    }

    public void saveQuoteComment() {
        saveToCache(qList, mList);
    }

    private void saveToCache(List<Comment> qComment, List<Comment> MyComment) {
        QuoteListCache.putQuoteQComment(usersId, qComment, context);
        QuoteListCache.putQuoteMyComment(usersId, MyComment, context);
    }

    public void load(boolean loadMore){
        load(loadMore, PAGE_SIZE);
    }

    /**
     * load from server
     *
     * @param loadMore true is loadMore, false is refresh or change
     */
    public void load(boolean loadMore, int pageSize) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        this.loadMore = loadMore;
        mListChangeListener.onListLoadStarted();
        QuoteRequest request;
        if (loadMore) {
            if (!canLoadMore) {
                isLoading = false;
                mListChangeListener.onListLoadFinished();
                return;
            }
            ++pageNO;
        } else {
            pageNO = 1;
        }
        request = new QuoteRequest(pageNO, pageSize);
        startRequest(request, loadMore);
    }

    private void onResponse(boolean success, boolean loadMore, Object response, VolleyError error) {
        if (success) {
            canLoadMore = (pageNO * PAGE_SIZE) < ((QuoteResult) response).data.page.totalCount;

            qList = ((QuoteResult) response).data.page.commentList;
            mList = ((QuoteResult) response).data.page.quoteCommentList;

//            SparseArray<Comment> qArray = new SparseArray<>();

            if (loadMore) {
                mListChangeListener.onListAppend(qList, mList);
            } else {
                SparseArray<Comment> mArray = new SparseArray<>();
                for (int i = 0; i < mList.size(); i++) {
                    mArray.put(mList.get(i).id, mList.get(i));
                }
                mListChangeListener.onListChanged(qList, mArray);
            }
        } else {
            mListChangeListener.onListLoadError(error);
        }

        isLoading = false;
        mListChangeListener.onListLoadFinished();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean loadMore() {
        return loadMore;
    }

    public boolean canLoadMore() {
        return canLoadMore;
    }

    public boolean isEmpty() {
        return qList.isEmpty() || qList.size() == 0;
    }

    private void startRequest(Request<QuoteResult> request, final boolean loadMore) {
        Volley.getInstance().addToRequestQueue(request);
        request.setListener(new Response.Listener<QuoteResult>() {
            @Override
            public void onResponse(QuoteResult response) {
                QuoteListResource.this.onResponse(true, loadMore, response, null);
            }
        });
        request.setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResponse(false, loadMore, null, error);
            }
        });
    }

    public interface OnListChangeListener {
        void onListChanged(List<Comment> qComment, SparseArray<Comment> MyComment);

        void onListAppend(List<Comment> qComment, List<Comment> MyComment);

        void onListLoadFinished();

        void onListLoadStarted();

        void onListLoadError(VolleyError error);
    }
}
