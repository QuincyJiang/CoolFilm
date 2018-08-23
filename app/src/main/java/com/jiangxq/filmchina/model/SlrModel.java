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
 * Created by jiangxq170307 on 2017/9/19.
 */

public class SlrModel extends BaseModel {
    private List<String> pics = new ArrayList<>();
    private List<String> author = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> viewer = new ArrayList<>();
    private List<String> like = new ArrayList<>();
    private List<String> comment = new ArrayList<>();
    private List<String> uri = new ArrayList<>();
    private ArrayList<ArticaItemBean> articals = new ArrayList<ArticaItemBean>();
    private Document articalDoc;
    private Elements thumbnails;
    private Element content;
    public SlrModel(BaseFragment context) {
        super(context);
    }

    /**
     * 获取文章列表
     * @param mObservable
     * @param page
     * */
    public void getArticalList(int page,Observer<List<ArticaItemBean>> mObservable){
        ApiService service = MyApplication.myApp.getApiService();
        Observable<List<ArticaItemBean>> responseBodyObservable = service.getSlrList(page).map(new Func1<ResponseBody, List<ArticaItemBean>>() {
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
                    thumbnails = articalDoc.getElementsByTag("article");
                    for(Element element:thumbnails){
                        pics.add(element.getElementsByTag("div").get(0).getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src"));
                        content = element.getElementsByClass("content").get(0);

                        uri.add(content.getElementsByTag("div").get(0).getElementsByTag("h2").get(0).getElementsByTag("a").get(0).attr("href"));
                        title.add(content.getElementsByTag("div").get(0).getElementsByTag("h2").get(0).getElementsByTag("a").get(0).text());
                        author.add("");
                        date.add(content.getElementsByClass("data").get(0).getElementsByTag("span").get(0).text());
                        viewer.add(content.getElementsByClass("data").get(0).getElementsByTag("span").get(1).text());
                        comment.add(content.getElementsByClass("data").get(0).getElementsByTag("span").get(2).text());
                        like.add(content.getElementsByClass("data").get(0).getElementsByTag("span").get(3).text());
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

