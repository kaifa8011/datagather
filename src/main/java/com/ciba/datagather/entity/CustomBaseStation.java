package com.ciba.datagather.entity;

import com.ciba.datagather.constant.Constant;

/**
 * @author ciba
 * @description 自定义基站信息
 * @date 2018/12/3
 */
public class CustomBaseStation {
    private String bscid = Constant.GET_DATA_FAILED_MAYBE_NO_SIM;
    private String bsss = "";
    private String lac = Constant.GET_DATA_FAILED_MAYBE_NO_SIM;
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

    public String getBscid() {
        return bscid;
    }

    public void setBscid(String bscid) {
        this.bscid = bscid;
    }

    public String getBsss() {
        return bsss;
    }

    public void setBsss(String bsss) {
        this.bsss = bsss;
    }
}
