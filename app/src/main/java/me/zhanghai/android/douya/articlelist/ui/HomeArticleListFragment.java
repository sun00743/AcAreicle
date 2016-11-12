package me.zhanghai.android.douya.articlelist.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.articlelist.content.ArticleListResFragment;
import me.zhanghai.android.douya.articlelist.content.HomeArticleListResFragment;
import me.zhanghai.android.douya.main.ui.MainActivity;
import me.zhanghai.android.douya.util.FragmentUtils;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeArticleListFragment extends BaseArticleDesListFragment {

    private static final String EXTRA_CHANNEL_ID = HomeArticleListFragment.class.getName() + "channelId";
    private static final String EXTRA_SORT = HomeArticleListFragment.class.getName() + "sort";

    @BindDimen(R.dimen.toolbar_and_tab_height)
    int mToolbarAndTabHeight;

    private int channelId = 110;
    private int sort = 0;

    public static HomeArticleListFragment newInstance(int channelId, int sort){
        HomeArticleListFragment fragment = new HomeArticleListFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putInt(EXTRA_CHANNEL_ID,channelId);
        arguments.putInt(EXTRA_SORT,sort);
        fragment.setArguments(arguments);
        return fragment;
    }

    public HomeArticleListFragment() {}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        setPaddingTop(mToolbarAndTabHeight);
        channelId = getArguments().getInt(EXTRA_CHANNEL_ID);
        sort = getArguments().getInt(EXTRA_SORT);
    }

    @Override
    protected void onSwipeRefresh() {
        super.onSwipeRefresh();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.refreshNotificationList();
    }

    @Override
    protected ArticleListResFragment onAttachArticleListResource() {
        return HomeArticleListResFragment.attachTo(this,channelId,sort);
    }
}
