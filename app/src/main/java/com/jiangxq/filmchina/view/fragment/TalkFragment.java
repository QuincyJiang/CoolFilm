package com.jiangxq.filmchina.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.adapter.ArticalListAdapter;
import com.jiangxq.filmchina.base.BaseFragment;
import com.jiangxq.filmchina.model.TalkModel;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.presenter.ArticalListContract;
import com.jiangxq.filmchina.presenter.ArticalListPresenter;
import com.jiangxq.filmchina.util.CustomLoadMoreView;
import com.jiangxq.filmchina.util.Utils;
import com.jiangxq.filmchina.view.activity.ArticalDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by jiangxq170307 on 2017/9/19.
 */

public class TalkFragment extends BaseFragment implements ArticalListContract.View {
    @Bind(R.id.rv_artical_list)
    RecyclerView articalList;
    @Bind(R.id.sr_refresh)
    SwipeRefreshLayout refresh;
    private ArticalListAdapter mAdapter;
    private ArticalListPresenter mPresenter;
    private int mPage = 1;
    private List<ArticaItemBean> articalsItem = new ArrayList<>();
    private Boolean isErr = false;
    private int mCurrentCounter;
    private AlertDialog dailog;


    @Override
    protected int getLayout() {
        return R.layout.fragment_artical;
    }
    @Override
    protected void initData() {
        mPresenter = new ArticalListPresenter(new TalkModel(getContext()),this);
    }

    @Override
    protected void initView() {
        initRecyclerView();
        initAdapter();
        mPage = 1;
        articalList.setAdapter(mAdapter);
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadArtical(mPage);
    }

    private void initRecyclerView(){
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
    }
    private void initAdapter(){
        mAdapter = new ArticalListAdapter(R.layout.item_artical_list,articalsItem);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ArticalDetailActivity.class);
                intent.putExtra("artical",mAdapter.getData().get(position));
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter >= 1000) {
                    //数据全部加载完毕
                    mAdapter.loadMoreEnd();
                } else {
                    if (!isErr) {
                        mPresenter.loadArtical(mPage);
                    } else {
                        //获取更多数据失败
                        isErr = true;
                        mAdapter.loadMoreFail();

                    }
                }
            }},articalList);
    }
    private void initListener(){
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mPresenter!=null){
                    mPage = 1;
                    mAdapter.setEnableLoadMore(false);
                    articalsItem.clear();
                    mAdapter.setNewData(articalsItem);
                    mPresenter.loadArtical(mPage);
                    mPresenter.loadBanner();
                }
            }
        });
        articalList.addOnScrollListener(scrollListener);
    }

    @Override
    public void showArtical(List<ArticaItemBean> articals) {
        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
        mAdapter.setEnableLoadMore(true);
        mPage++;
        mAdapter.addData(articals);
        mCurrentCounter = mAdapter.getData().size();
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showBanners(final List<ArticaItemBean> banners) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void showDialog(String msg) {

    }

    @Override
    public void showError(String msg) {
        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);}
        if(dailog==null){
            dailog = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_content)+msg)
                    .setPositiveButton(getString(R.string.confirm), null)
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }else dailog.show();
    }


    @Override
    public void dismissLoading() {
        articalList.setVisibility(View.VISIBLE);

    }

    @Override
    public void dismissDialog() {
        if(dailog!=null){
            if(dailog.isShowing()){
                dailog.dismiss();
            }
        }
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
        }
        /**
         * 滑动状态改变监听 在用户滑动 拖动  惯性滑动的时候 暂停图片加载
         * */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case SCROLL_STATE_IDLE:
                    try {
                        if(getContext() != null) Glide.with(getContext()).resumeRequests();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_DRAGGING:
                    try {
                        if(getContext() != null) Glide.with(getContext()).pauseRequests();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_SETTLING:
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
