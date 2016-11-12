/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.home;

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
import me.zhanghai.android.douya.R;
import me.zhanghai.android.douya.articlelist.ui.HomeArticleListFragment;
import me.zhanghai.android.douya.ui.AppBarManager;
import me.zhanghai.android.douya.ui.AppBarWrapperLayout;
import me.zhanghai.android.douya.ui.TabFragmentPagerAdapter;
import me.zhanghai.android.douya.util.FileUtil;

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
        //noinspection deprecation
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

        FileUtil.deleteFiles(getActivity().getExternalCacheDir());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        mTabAdapter = new TabFragmentPagerAdapter(this);

        mTabAdapter.addTab(HomeArticleListFragment.newInstance(110,0), getString(R.string.home_mixmain));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(73,0), getString(R.string.home_bicanlvhua));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(74,0), getString(R.string.home_animation));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(75,0), getString(R.string.home_cartoon));
        mTabAdapter.addTab(HomeArticleListFragment.newInstance(63,0), getString(R.string.home_game));

//        mTabAdapter.addTab(new TabFragmentPagerAdapter.FragmentCreator() {
//            @Override
//            public Fragment createFragment() {
//                return HomeBroadcastListFragment.newInstance();
//            }
//        }, getString(R.string.home_broadcast));

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

    public void currentGoTop(){
        if(mTabAdapter != null && mTabAdapter.getCount()>0 && mViewPager != null){
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
}
