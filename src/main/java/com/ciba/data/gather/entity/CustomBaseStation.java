package com.ciba.data.gather.entity;

import com.ciba.data.gather.constant.Constant;

/**
 * @author ciba
 * @description 自定义基站信息
 * @date 2018/12/3
 */
public class CustomBaseStation {
    private String bsss = "";
    private String lac = "";
    private String stbif = "";

    public String getStbif() {
        return stbif;
    }

    public void setStbif(String stbif) {
        this.stbif = stbif;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getBsss() {
        return bsss;
    }

    public void setBsss(String bsss) {
        this.bsss = bsss;
    }
}
