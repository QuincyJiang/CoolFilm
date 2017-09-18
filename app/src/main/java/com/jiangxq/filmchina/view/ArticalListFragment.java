package com.jiangxq.filmchina.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.adapter.RecyclerViewAdapter;
import com.jiangxq.filmchina.base.BaseFragment;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.model.bean.BannerData;
import com.jiangxq.filmchina.presenter.ArticalListContract;
import com.jiangxq.filmchina.presenter.ArticalListPresenter;
import com.jiangxq.filmchina.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public class ArticalListFragment extends BaseFragment implements ArticalListContract.View{
    @Bind(R.id.rv_artical_list)
    RecyclerView articalList;
    @Bind(R.id.sr_refresh)
    SwipeRefreshLayout refresh;
    private RecyclerViewAdapter adapter;
    private boolean loading;
    private int loadTimes;
    private BannerData bannerData =new BannerData();
    private ArticalListPresenter mPresenter;
    private int mPage = 1;
    List<String> pics = new ArrayList<>();
    List<String> titles =  new ArrayList<>();
    List<String> herfs =  new ArrayList<>();
    private List<ArticaItemBean> articalsItem = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_artical;
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

        adapter = new RecyclerViewAdapter(getContext());
        adapter.setItems(articalsItem);
        initArtical();
        initBanner();
        adapter.addFooter();
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
        adapter.setBanner(bannerData);
        adapter.notifyDataSetChanged();
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
    public void showError(String msg) {

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

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (!loading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                if (loadTimes <= 5) {
                            adapter.removeFooter();
                            loading = false;
                            adapter.addFooter();
                            mPresenter.loadArtical(mPage);
                            loadTimes++;
                        } else {
                            adapter.removeFooter();}
                loading = true;
            }
        }
    };

}
