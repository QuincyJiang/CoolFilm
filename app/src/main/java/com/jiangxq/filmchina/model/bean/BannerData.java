package com.jiangxq.filmchina.model.bean;

import java.util.List;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public class BannerData {
    List<String> pics;
    List<String> title;
    List<String> herf;

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getHerf() {
        return herf;
    }

    public void setHerf(List<String> herf) {
        this.herf = herf;
    }
}
