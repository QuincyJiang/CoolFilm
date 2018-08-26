package com.jiangxq.filmchina.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.base.BaseActivity;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.presenter.ArticalDetailContract;
import com.jiangxq.filmchina.presenter.ArticalDetailPresenter;
import com.jiangxq.filmchina.util.DensityUtils;
import com.jiangxq.filmchina.view.widget.ItemOnLongClickedPopWindow;
import com.jiangxq.filmchina.view.widget.NestedScrollWebView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import butterknife.Bind;
import rx.functions.Action1;

import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static android.view.View.GONE;


/**
 * Created by jiangxq170307 on 2017/9/16.
 */

public class ArticalDetailActivity extends BaseActivity implements ArticalDetailContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.wv_container)
    FrameLayout webViewContainer;
    @Bind(R.id.fl_loading)
    FrameLayout loading;
    @Bind(R.id.pb_loading)
    ProgressBar progressBar;
    @Bind(R.id.rl_error)
    RelativeLayout errorPage;
    @Bind(R.id.tv_detail_date)
    TextView mDate;
    @Bind(R.id.tv_detail_title)
    TextView mTitle;

    private ArticaItemBean articalData;
    private NestedScrollWebView articalContent;
    private ArticalDetailPresenter mPresenter;
    private static String imgUrl;
    private ItemOnLongClickedPopWindow itemLongClickedPopWindow;
    private SaveImage mSaveImageTask;
    private RxPermissions rxPermissions;
    private AlertDialog alertDialog;
    private AlertDialog savePicDialog;



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
        if(progressBar!=null){
            progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK,MULTIPLY);
        }
        if(articalContent==null){
            articalContent = new NestedScrollWebView(this);
        }
        initWebView();
        errorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                mPresenter.loadArtical(articalData.getUri().split("com/")[1]);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share){
            if(articalData!=null){
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_content),articalData.getTitle(),
                    articalData.getUri()));
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, getString(R.string.share_article)));
            return true;
        }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
        rxPermissions = new RxPermissions(this);
        mPresenter = new ArticalDetailPresenter(this,this);
        articalData = (ArticaItemBean)getIntent().getSerializableExtra("artical");
        mDate.setText(articalData.getDate());
        mTitle.setText(articalData.getTitle());
        mPresenter.loadArtical(articalData.getUri().split("com/")[1]);
    }

    @Override
    public void dismissLoading() {
        if(errorPage!=null&&loading!=null){
            loading.setVisibility(GONE);
            errorPage.setVisibility(GONE);
        }
        webViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        if(errorPage!=null&&loading!=null){
            errorPage.setVisibility(View.VISIBLE);
            loading.setVisibility(GONE);
        }
        webViewContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showArtical(String html) {
        articalContent.loadDataWithBaseURL(null,html,"text/html", "utf-8",null);
    }

    @Override
    public void showPermissionDialog() {
        if(alertDialog==null){
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permissions_grant_fail))
                    .setMessage(getString(R.string.permissions_description))
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rxPermissions
                                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {
                                            if(aBoolean){
                                                mSaveImageTask = null;
                                                mSaveImageTask = new SaveImage(ArticalDetailActivity.this);
                                                mSaveImageTask.execute();
                                            }else{
                                                showPermissionDialog();
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }else alertDialog.show();
    }

    public void showSavePicDialog(){
        if(savePicDialog==null){
            savePicDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.save_pic_title))
                    .setMessage(String.format(getString(R.string.save_pic_content),Environment.getExternalStorageDirectory()+"/Download"))
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            rxPermissions
                                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {
                                            if(aBoolean){
                                                mSaveImageTask = null;
                                                mSaveImageTask = new SaveImage(ArticalDetailActivity.this);
                                                mSaveImageTask.execute();
                                            }else{
                                                showPermissionDialog();
                                            }
                                        }
                                    });

                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }else savePicDialog.show();
    }

    @Override
    public void showLoading() {
        if(errorPage!=null&&loading!=null){
            loading.setVisibility(View.VISIBLE);
            errorPage.setVisibility(GONE);
        }
        webViewContainer.setVisibility(View.INVISIBLE);
    }
    private void initWebView(){
        articalContent.setNestedScrollingEnabled(false);
        webViewContainer.addView(articalContent);
        WebSettings settings = articalContent.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        articalContent.setWebViewClient(new CustomWebViewClient());
        articalContent.setWebChromeClient(new MyWebChromeClient());
        articalContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        articalContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                WebView.HitTestResult result = ((WebView)view).getHitTestResult();
                return handleClick(view,result);
            }
        });
    }
    private boolean handleClick(View v,WebView.HitTestResult result){
        if (null == result)
            return false;
        int type = result.getType();
        if (type == WebView.HitTestResult.UNKNOWN_TYPE)
            return false;
        /**
         * 文本信息不作处理 由webkit自动调取复制分享对话框
         * */
        if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {
            return false;
        }
        if(itemLongClickedPopWindow==null){
            itemLongClickedPopWindow = new ItemOnLongClickedPopWindow(ArticalDetailActivity.this,ItemOnLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW, DensityUtils.dp2px(this,150), DensityUtils.dp2px(this,80));
        }
        switch (type) {
            case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                break;
            case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                break;
            case WebView.HitTestResult.GEO_TYPE: // TODO
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                break;
            case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                imgUrl = result.getExtra();
                showSavePicDialog();
                break;
            default:
                break;
        }

        itemLongClickedPopWindow.getView(R.id.item_longclicked_viewImage)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemLongClickedPopWindow.dismiss();
                    }
                });
        itemLongClickedPopWindow.getView(R.id.item_longclicked_saveImage)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemLongClickedPopWindow.dismiss();
                        rxPermissions
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if(aBoolean){
                                            mSaveImageTask = null;
                                            mSaveImageTask = new SaveImage(ArticalDetailActivity.this);
                                            mSaveImageTask.execute();
                                        }else{
                                            showPermissionDialog();
                                        }
                                    }
                                });

                    }
                });
        return true;
    }




    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(articalContent!=null){
            if ((keyCode == KeyEvent.KEYCODE_BACK) && articalContent.canGoBack()) {
                articalContent.goBack();
                return true;
            }else if(keyCode == KeyEvent.KEYCODE_BACK){
                clearWebView();
                finish();
                return true;
            }
        }
        return false;
    }
    private void clearWebView(){
        if(articalContent!=null){
            articalContent.removeAllViews();
            articalContent.destroy();
        articalContent=null;}
    }
    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress>70){
                dismissLoading();
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    /**
     * webview 加载时会回调相应方法
     * */
    private class CustomWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
//           dismissLoading();
        }
    }

    /**
     * 为了防止activity销毁而webview不销毁导致内存泄露
     * */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSaveImageTask!=null){
            mSaveImageTask =null;
        }
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
     private static class SaveImage extends AsyncTask<String, Void, String> {
         private WeakReference<ArticalDetailActivity> mContext;

         public SaveImage(ArticalDetailActivity activity) {
             this.mContext = new WeakReference<>(activity);
         }

         @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }
                int idx = imgUrl.lastIndexOf(".");
                String ext = imgUrl.substring(idx);
                file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
                InputStream inputStream = null;
                URL url = new URL(imgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = "图片已保存至：" + file.getAbsolutePath();
            } catch (Exception e) {
                result = "保存失败！" + e.getLocalizedMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mContext.get(),result,Toast.LENGTH_SHORT).show();
        }
    }
}

