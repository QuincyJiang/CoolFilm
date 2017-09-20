package com.jiangxq.filmchina.model;

import com.jiangxq.filmchina.api.ApiService;
import com.jiangxq.filmchina.app.MyApplication;
import com.jiangxq.filmchina.base.BaseFragment;
import com.jiangxq.filmchina.base.BaseModel;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalListModel extends BaseModel{
    private List<String> bannerUrls = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();
    private List<String> bannerHerfs = new ArrayList<>();
    private List<String> bannerAuthors = new ArrayList<>();
    private List<String> pics = new ArrayList<>();
    private List<String> author = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> viewer = new ArrayList<>();
    private List<String> like = new ArrayList<>();
    private List<String> comment = new ArrayList<>();
    private List<String> uri = new ArrayList<>();
    private ArrayList<ArticaItemBean> mBanners = new ArrayList<>();
    private ArrayList<ArticaItemBean> articals = new ArrayList<ArticaItemBean>();
    private Document articalDoc;
    private Document bannerDoc;
    private Elements thumbnails;
    private Elements titles;
    private Elements desc;
    private Elements urlTarget;
    private Elements titleTarget;
    private Elements authorTarget;
    private String originString;
    public ArticalListModel(BaseFragment context) {
       super(context);
    }

    /**
     * 取banner
     * @param mObservable
     */
    public void getHomeBanner(Observer<List<ArticaItemBean>> mObservable) {
        ApiService service = MyApplication.myApp.getApiService();
        Observable<List<ArticaItemBean>> responseBodyObservable = service.getBanner().map(new Func1<ResponseBody, List<ArticaItemBean>>() {

            @Override
            public List<ArticaItemBean> call(ResponseBody responseBody) {
                bannerUrls.clear();
                bannerAuthors.clear();
                bannerHerfs.clear();
                bannerTitles.clear();
                mBanners.clear();
                try {
                    bannerDoc = Jsoup.parse(responseBody.string());
                    urlTarget = bannerDoc.getElementsByClass("entry-featured__thumbnail");
                    for(Element element:urlTarget){
                        originString = element.attr("style");
                        bannerUrls.add(originString.substring(originString.indexOf("url('")+5,originString.indexOf(".jpg")+4));
                    }
                    titleTarget = bannerDoc.getElementsByClass("entry-title entry-featured__title");
                    for(Element element:titleTarget){

                        bannerTitles.add( element.select("a").text());
                        bannerHerfs.add(element.select("a").attr("href"));
                    }
                    authorTarget = bannerDoc.getElementsByClass("featured-post-meta");
                    for(Element element:authorTarget){
                        bannerAuthors.add(element.text());
                    }
                    for(int i = 0;i<bannerTitles.size();i++){
                        ArticaItemBean banner = new ArticaItemBean();
                        banner.setTitle(bannerTitles.get(i));
                        banner.setPic(bannerUrls.get(i));
                        banner.setAuthor(bannerAuthors.get(i));
                        banner.setUri(bannerHerfs.get(i));
                        banner.setViewer("");
                        banner.setComment("");
                        banner.setLike("");
                        banner.setDate("");
                        mBanners.add(banner);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return mBanners;
            }

        });


        subscribe(responseBodyObservable, mObservable);
    }

    /**
     * 获取文章列表
     * @param mObservable
     * @param page
     * */
    public void getArticalList(int page,Observer<List<ArticaItemBean>> mObservable){
    ApiService service = MyApplication.myApp.getApiService();
    Observable<List<ArticaItemBean>> responseBodyObservable = service.getArticalList(page).map(new Func1<ResponseBody, List<ArticaItemBean>>() {
        @Override
        public List<ArticaItemBean> call(ResponseBody responseBody) {
            pics.clear();
            author.clear();
            title.clear();
            date.clear();
            viewer.clear();
            like.clear();
            comment.clear();
            articals.clear();
            uri.clear();
            try {
                articalDoc  = Jsoup.parse(responseBody.string());
                thumbnails = articalDoc.getElementsByClass("entry-thumbnail entry-media-image ");
                for(Element element:thumbnails){
                    uri.add(element.select("a").attr("href"));
                    pics.add(element.select("a").select("img").attr("src"));
                }
                titles = articalDoc.getElementsByClass("entry-title h2");
                for (Element element:titles){
                    title.add(element.select("a").text());
                }
                desc = articalDoc.getElementsByTag("span");
                for(Element element:desc){
                    switch (element.className()){
                        case "author vcard":
                            author.add(element.select("a").text());
                            break;
                        case "meta-date posted-on":
                            date.add(element.select("a").select("time").text());
                            break;
                        case "meta-view":
                            viewer.add(element.text());
                            break;
                        case "meta-comments":
                            comment.add(element.select("a").text());
                            break;
                        case "dot-irecommendthis-count":
                            like.add(element.text());
                            break;
                    }
                }
                for(int i = 0;i< pics.size();i++){
                    ArticaItemBean artical = new ArticaItemBean();
                    artical.setPic(pics.get(i));
                    artical.setAuthor(author.get(i));
                    artical.setTitle(title.get(i));
                    artical.setComment(comment.get(i));
                    artical.setDate(date.get(i));
                    artical.setLike(like.get(i));
                    artical.setUri(uri.get(i));
                    artical.setViewer(viewer.get(i));
                    articals.add(artical);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return articals;
        }
    });
        subscribe(responseBodyObservable, mObservable);
    }
}
