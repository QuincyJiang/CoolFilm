package com.jiangxq.filmchina.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by jiangxq170307 on 2017/9/19.
 */

public class LoveLifeFragment extends BaseFragment {
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager mViewPager;
    @Override
    protected int getLayout() {
        return R.layout.activity_tab_layout;
    }

    @Override
    protected void initView() {
        LoveLifePagerAdapter mSectionsPagerAdapter = new LoveLifePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
    public class LoveLifePagerAdapter extends FragmentPagerAdapter {

        public LoveLifePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new OriginalFragment();
                case 1:
                    return new TalkFragment();
                case 2:
                    return new MovieFragment();
                default:
                    return new OriginalFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.love_life_1);
                case 1:
                    return getString(R.string.love_life_2);
                case 2:
                    return getString(R.string.love_life_3);

            }
            return "";
        }
    }}
