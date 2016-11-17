/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mika.com.android.ac.articlelist.ui.HomeArticleListFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<HomeArticleListFragment> mFragmentCreatorList = new ArrayList<>();
    private List<CharSequence> mTitleList = new ArrayList<>();

    @Deprecated
    public TabFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public TabFragmentPagerAdapter(FragmentActivity activity) {
        //noinspection deprecation
        this(activity.getSupportFragmentManager());
    }

    public TabFragmentPagerAdapter(Fragment fragment) {
        //noinspection deprecation
        this(fragment.getChildFragmentManager());
    }

//    public void addTab(FragmentCreator fragmentCreator, String title) {
//        mFragmentCreatorList.add(fragmentCreator);
//        mTitleList.add(title);
//    }

    public void addTab(HomeArticleListFragment homeArticleListFragment, String title) {
        mFragmentCreatorList.add(homeArticleListFragment);
        mTitleList.add(title);
    }

    @Override
    public HomeArticleListFragment getItem(int position) {
//        return mFragmentCreatorList.get(position).createFragment();
        return mFragmentCreatorList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentCreatorList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    /**
     * @deprecated Use {@link #setPageTitle(TabLayout, int, CharSequence)} instead.
     */
    public void setPageTitle(int position, CharSequence title) {
        mTitleList.set(position, title);
    }

    public void setPageTitle(TabLayout tabLayout, int position, CharSequence title) {
        //noinspection deprecation
        setPageTitle(position, title);
        if (position < tabLayout.getTabCount()) {
            tabLayout.getTabAt(position).setText(title);
        }
    }

    public interface FragmentCreator {
        Fragment createFragment();
    }
}
