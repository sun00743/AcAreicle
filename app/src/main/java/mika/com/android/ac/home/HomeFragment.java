
/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.R;
import mika.com.android.ac.articlelist.content.ListSort;
import mika.com.android.ac.articlelist.ui.HomeArticleListFragment;
import mika.com.android.ac.ui.AppBarManager;
import mika.com.android.ac.ui.AppBarWrapperLayout;
import mika.com.android.ac.ui.TabFragmentPagerAdapter;
import mika.com.android.ac.util.FileUtil;

public class HomeFragment extends Fragment implements AppBarManager {

    @BindView(R.id.appBarWrapper)
    AppBarWrapperLayout mAppBarWrapperLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private TabFragmentPagerAdapter mTabAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //clear cache
        FileUtil.deleteFiles(getActivity().getExternalCacheDir());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        mTabAdapter = new TabFragmentPagerAdapter(this);
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(ListSort.Channels.HOME, ListSort.Sort.NEWS), getString(R.string.home_mixmain));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(ListSort.Channels.GREEN, ListSort.Sort.NEWS), getString(R.string.home_bicanlvhua));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(ListSort.Channels.ANIMATION, ListSort.Sort.NEWS), getString(R.string.home_animation));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(ListSort.Channels.FICTION, ListSort.Sort.NEWS), getString(R.string.home_cartoon));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(ListSort.Channels.GAME, ListSort.Sort.NEWS), getString(R.string.home_game));

        mViewPager.setOffscreenPageLimit(mTabAdapter.getCount() - 1);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                showAppBar();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void currentGoTop() {
        if (mTabAdapter != null && mTabAdapter.getCount() > 0 && mViewPager != null) {
            mTabAdapter.getItem(mViewPager.getCurrentItem()).scrollToTop();
        }
    }

    @Override
    public void showAppBar() {
        mAppBarWrapperLayout.show();
    }

    @Override
    public void hideAppBar() {
        mAppBarWrapperLayout.hide();
    }

    /**
     * article sort menu item select
     * @param sort sort
     */
    public void onArticleSortChanged(int sort) {
        if (mTabAdapter != null && mTabAdapter.getCount() > 0) {
            for (int i = 0; i < mTabAdapter.getCount(); i++) {
                mTabAdapter.getItem(i).onSortChanged(sort);
            }
        }
    }
}
