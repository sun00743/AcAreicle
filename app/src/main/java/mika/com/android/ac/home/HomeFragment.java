
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //clear cache
        FileUtil.deleteFiles(getActivity().getExternalCacheDir());

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
