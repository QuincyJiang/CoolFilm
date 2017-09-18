package com.jiangxq.filmchina.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.adapter.RecyclerViewAdapter2;
import com.jiangxq.filmchina.base.BaseFragment;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.model.bean.BannerData;
import com.jiangxq.filmchina.presenter.ArticalListContract;
import com.jiangxq.filmchina.presenter.ArticalListPresenter;
import com.jiangxq.filmchina.util.GlideImageLoader;
import com.jiangxq.filmchina.util.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;


/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public class ArticalListFragment2 extends BaseFragment implements ArticalListContract.View{
    @Bind(R.id.rv_artical_list)
    RecyclerView articalList;
    @Bind(R.id.sr_refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.banner)
    Banner banner;
    private RecyclerViewAdapter2 adapter;
    private boolean loading;
    private int loadTimes;
    private BannerData bannerData =new BannerData();
    private ArticalListPresenter mPresenter;
    private int mPage = 1;
    List<String> pics = new ArrayList<>();
    List<String> titles =  new ArrayList<>();
    List<String> herfs =  new ArrayList<>();
    private int bannerHeight;
    private List<ArticaItemBean> articalsItem = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_artical2;
    }
    @Override
    protected void initData() {
        mPresenter = new ArticalListPresenter(getActivity(),this);
    }

    @Override
    protected void initView() {
        if (Utils.getScreenWidthDp(getContext()) >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            articalList.setLayoutManager(gridLayoutManager);
        } else if (Utils.getScreenWidthDp(getContext()) >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            articalList.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            articalList.setLayoutManager(linearLayoutManager);
        }
        initBannerView();
        adapter = new RecyclerViewAdapter2(getContext());
        initArtical();
        initBanner();
        articalList.setAdapter(adapter);
        refresh.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mPresenter!=null){
                    initArtical();
                    mPresenter.loadArtical(mPage);
                }
            }
        });
        articalList.addOnScrollListener(scrollListener);
        mPresenter.loadBanner();
        mPresenter.loadArtical(mPage);
    }
    private void initBannerView(){
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ZoomOutSlide);

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        bannerHeight = banner.getHeight();
    }

    @Override
    public void showArtical(List<ArticaItemBean> articals) {
        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
        if(adapter.getSize()!=0){
            adapter.removeFooter();
        }
        mPage++;
        adapter.addItems(articals);
    }

    @Override
    public void showBanners(List<ArticaItemBean> banners) {
        initBanner();
        for(ArticaItemBean bannerBean:banners){
            titles.add(bannerBean.getTitle());
            pics.add(bannerBean.getPic());
            herfs.add(bannerBean.getUri());
        }
        bannerData.setHerf(herfs);
        bannerData.setPics(pics);
        bannerData.setTitle(titles);
        banner.setImages(bannerData.getPics());
        banner.setBannerTitles(bannerData.getTitle());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        banner.start();
    }

    private void initBanner(){
        if(titles!=null&&pics!=null&&herfs!=null){
            titles.clear();
            pics.clear();
            herfs.clear();
        }
    }
    private void initArtical(){
        if(adapter.getSize()!=0){
            adapter.clearItems();
        }
        mPage=1;
    }

    @Override
    public void showDialog(String msg) {

    }

    @Override
    public void dismissLoading() {

        articalList.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void showLoading() {

        articalList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String msg) {

    }

    private int maxDistance = 255;
    private int distance = 0;
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            distance += dy;
            float percent = distance * 1f / maxDistance;
            ViewGroup.LayoutParams params = banner.getLayoutParams();
            params.height = (int) percent*bannerHeight;
            banner.setLayoutParams(params);
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            boolean isloading = loading;
            int count = linearLayoutManager.getItemCount();
            int posotion = linearLayoutManager.findLastVisibleItemPosition();
            if (!loading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                if (loadTimes <= 5) {
                            if(adapter.getSize()!=0){
                                adapter.removeFooter();
                            }
                            loading = true;
                            adapter.addFooter();
                            mPresenter.loadArtical(mPage);
                            loadTimes++;
                        } else {
                            if(adapter.getSize()!=0){
                        adapter.removeFooter();
                    }}
                loading = false;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                    //当屏幕停止滚动，加载图片
                    try {
                        if(getContext() != null) Glide.with(getContext()).resumeRequests();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                    //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                    try {
                        if(getContext() != null) Glide.with(getContext()).pauseRequests();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                    //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                    try {
                        if(getContext() != null) Glide.with(getContext()).pauseRequests();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };



}
