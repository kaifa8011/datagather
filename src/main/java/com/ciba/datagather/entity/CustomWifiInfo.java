package com.ciba.datagather.entity;

/**
 * @author ciba
 * @description 自定义WiFi信息
 * @date 2018/12/3
 */
public class CustomWifiInfo {
    private String ssid = "";
    private String bssid = "";
    private String networkId = "";
    private int linkSpeed;
    private int rssi;

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(int linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
