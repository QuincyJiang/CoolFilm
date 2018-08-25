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
 * Created by jiangxq170307 on 2017/9/18.
 */

public class LoveEquipmentFragment extends BaseFragment {
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
        LoveEquipmentPagerAdapter mSectionsPagerAdapter = new LoveEquipmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
    public class LoveEquipmentPagerAdapter extends FragmentPagerAdapter {

        public LoveEquipmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new SlrFragment();
                case 1:
                    return new TlrFragment();
                case 2:
                    return new PocketCameraFragment();
                case 3:
                    return new RangefinderFragment();
                default:
                    return new SlrFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.slr);
                case 1:
                    return getString(R.string.tlr);
                case 2:
                    return getString(R.string.pocket_cam);
                case 3:
                    return getString(R.string.rangefinder_cam);
            }
            return "";
        }
    }
}
