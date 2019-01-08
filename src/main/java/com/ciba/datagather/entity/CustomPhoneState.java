package com.ciba.datagather.entity;

import android.text.TextUtils;

import com.ciba.datagather.constant.Constant;

/**
 * @author ciba
 * @description 自定义收集状态信息
 * @date 2018/12/3
 */
public class CustomPhoneState {
    private String iccid = "";
    private String imsi = "";
    private String imei1 = "";
    private String imei2 = "";
    private String meid = "";
    private String imei = "";
    private String mcc = "";
    private String androidId = "";
    private int isNetworkRoaming;
    private int deviceType;

    public int getIsNetworkRoaming() {
        return isNetworkRoaming;
    }

    public void setIsNetworkRoaming(int isNetworkRoaming) {
        this.isNetworkRoaming = isNetworkRoaming;
    }

    public String getMcc() {
        if (Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION.equals(imsi) || Constant.GET_DATA_FAILED_MAYBE_NO_SIM.equals(imsi)) {
            mcc = imsi;
        } else if (imsi != null && imsi.length() > 3) {
            mcc = imsi.substring(0, 3);
        }
        return mcc;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getImei() {
        if (!Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION.equals(imei1)
                && !Constant.GET_DATA_FAILED_MAYBE_NO_SIM.equals(imei1)
                && !Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION.equals(imei2)
                && !Constant.GET_DATA_FAILED_MAYBE_NO_SIM.equals(imei2)) {
            if (!TextUtils.isEmpty(imei1) && !TextUtils.isEmpty(imei2)) {
                return imei1 + "," + imei2;
            } else if (!TextUtils.isEmpty(imei1)) {
                return imei1;
            }
        }
        return imei;
    }

    public String getRealImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
