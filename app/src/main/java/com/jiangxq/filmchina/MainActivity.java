package com.jiangxq.filmchina;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiangxq.filmchina.app.Constants;
import com.jiangxq.filmchina.base.BaseActivity;
import com.jiangxq.filmchina.util.DataCleanerManager;
import com.jiangxq.filmchina.view.activity.AboutActivity;
import com.jiangxq.filmchina.view.fragment.ActivitiesFragment;
import com.jiangxq.filmchina.view.fragment.LoveEquipmentFragment;
import com.jiangxq.filmchina.view.fragment.LoveLifeFragment;
import com.jiangxq.filmchina.view.fragment.LovePhotosFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @Bind(R.id.container)
    ViewPager mViewPager;
    @Bind(R.id.bottom_navigation)
    BottomNavigationView navigation;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected int getView() {
        return R.layout.activity_main_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);    }

    @Override
    protected void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.bottom_main_page);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.bottom_love_equipment);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.bottom_love_life);
                    break;
                case 3:
                    navigation.setSelectedItemId(R.id.bottom_activities);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_main_page:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.bottom_love_equipment:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.bottom_love_life:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.bottom_activities:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_film) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constants.ABOUT_FILM));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);        }
        else if (id == R.id.nav_about_coder) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_clear_cache) {
            showCleanCacheDialog();
        } else if (id == R.id.nav_exit) {
            System.exit(0);
        } else if (id == R.id.nav_share) {

        }else if(id == R.id.nav_update){

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private String getAppCacheSize() {

        try {
            long cacheSize = DataCleanerManager.getFolderSize(getApplicationContext().getCacheDir()) +
                    DataCleanerManager.getFolderSize(getApplicationContext().getExternalCacheDir())
                    +DataCleanerManager.getFolderSize(Glide.getPhotoCacheDir(getApplicationContext()));
            return DataCleanerManager.getFormatSize(cacheSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "--B";
    }

    private void showCleanCacheDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.clear_cache))
                .setMessage(String.format(getString(R.string.clear_cache_description),getAppCacheSize()))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanerManager.clearAllCache(getApplicationContext());
                            }
                        }).start();
                        Toast.makeText(MainActivity.this,"缓存已清理",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new LovePhotosFragment();
                case 1:
                    return new LoveEquipmentFragment();
                case 2:
                    return new LoveLifeFragment();
                case 3:
                    return new ActivitiesFragment();
                default:return new LovePhotosFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}
