package com.jiangxq.filmchina.view.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.base.BaseActivity;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.presenter.ArticalDetailContract;
import com.jiangxq.filmchina.presenter.ArticalDetailPresenter;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.Field;

import butterknife.Bind;


/**
 * Created by jiangxq170307 on 2017/9/16.
 */

public class ArticalDetailActivity extends BaseActivity implements ArticalDetailContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.wv_container)
    FrameLayout webViewContainer;
    private ArticaItemBean articalData;
    private WebView articalContent;
    private ProgressDialog dailog;
    private ArticalDetailPresenter mPresenter;

    @Override
    protected int getView() {
        return R.layout.activity_details_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        toolbar.setTitle(R.string.artical_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticalDetailActivity.this.finish();
            }
        });
        if(articalContent==null){
            articalContent = new WebView(this);
        }
        initWebView();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new ArticalDetailPresenter(this,this);
        articalData = (ArticaItemBean)getIntent().getSerializableExtra("artical");
        Logger.t(TAG).e("intentData-------->"+articalData.getTitle()+articalData.getUri());
        mPresenter.loadArtical(articalData.getUri().split("com/")[1]);
    }

    @Override
    public void dismissLoading() {
        if(dailog!=null&&dailog.isShowing()){
            dailog.dismiss();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void showArtical(String html) {
        String result = html;
        articalContent.loadDataWithBaseURL(null,html,"text/html", "utf-8",null);
//        articalContent.loadData(html,"text/html","UTF-8");
    }

    @Override
    public void showLoading() {
        if(dailog==null){
            dailog = new ProgressDialog(this);
        }
        dailog.setMessage(getString(R.string.loading));
        if(!dailog.isShowing()){
            dailog.show();
        }
    }
    private void initWebView(){
        webViewContainer.addView(articalContent);
        WebSettings settings = articalContent.getSettings();
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        articalContent.setWebViewClient(new CustomWebViewClient());
        articalContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                WebView.HitTestResult result = ((WebView)view).getHitTestResult();
                int type = result.getType();
                String imgurl = result.getExtra();
                return true;
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(articalContent!=null){
            if ((keyCode == KeyEvent.KEYCODE_BACK) && articalContent.canGoBack()) {
                articalContent.goBack();
                return true;
            }}
        clearWebView();
        finish();
        return false;
    }
    private void clearWebView(){
        if(articalContent!=null){
            articalContent.removeAllViews();
            articalContent.destroy();}
        articalContent = null;
    }
    /**
     * webview 加载时会回调相应方法
     * */
    private class CustomWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
           dismissLoading();
        }
    }

    /**
     * 为了防止activity销毁而webview不销毁导致内存泄露
     * */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch(Exception e) {
        }
    }
}

