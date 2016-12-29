/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.articlelist.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.articlelist.content.ArticleListResFragment;
import mika.com.android.ac.articlelist.content.HomeArticleListResFragment;
import mika.com.android.ac.main.ui.MainActivity;
import mika.com.android.ac.util.FragmentUtils;

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

    public static HomeArticleListFragment newInstance(int channelId, int sort) {
        HomeArticleListFragment fragment = new HomeArticleListFragment();
        Bundle arguments = FragmentUtils.ensureArguments(fragment);
        arguments.putInt(EXTRA_CHANNEL_ID, channelId);
        arguments.putInt(EXTRA_SORT, sort);
        fragment.setArguments(arguments);
        return fragment;
    }

    public HomeArticleListFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setPaddingTop(mToolbarAndTabHeight);
        channelId = getArguments().getInt(EXTRA_CHANNEL_ID);
        sort = getArguments().getInt(EXTRA_SORT);
    }

    @Override
    protected void onSwipeRefresh() {
        super.onSwipeRefresh();
        ((MainActivity) getActivity()).refreshNotificationList();
    }

    @Override
    protected ArticleListResFragment onAttachArticleListResource() {
        return HomeArticleListResFragment.attachTo(this, channelId, sort);
    }
}
