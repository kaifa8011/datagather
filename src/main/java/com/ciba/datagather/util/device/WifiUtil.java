package com.ciba.datagather.util.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;

import com.ciba.datagather.entity.CustomWifiInfo;
import com.ciba.datagather.common.DataGatherManager;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : 获取WiFi数据的工具类
 */

public class WifiUtil {
    public static CustomWifiInfo getWifiInfo() {
        CustomWifiInfo customWifiInfo = new CustomWifiInfo();
        try {
            int permission = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.ACCESS_WIFI_STATE);
            WifiManager wifiManager = (WifiManager) DataGatherManager.getInstance().getContext().getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null || permission != PackageManager.PERMISSION_GRANTED) {
                return customWifiInfo;
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            String bssid = wifiInfo.getBSSID();
            customWifiInfo.setBssid(bssid);

            String networkId = wifiInfo.getNetworkId() + "";
            customWifiInfo.setNetworkId(networkId);

            customWifiInfo.setLinkSpeed(wifiInfo.getLinkSpeed());

            customWifiInfo.setRssi(wifiInfo.getRssi());

            // get ssid
            String ssid = wifiInfo.getSSID();
            if (ssid.startsWith("\"")) {
                ssid = ssid.replaceFirst("\"", "");
                if (ssid.endsWith("\"")) {
                    ssid = ssid.substring(0, ssid.length() - 1);
                }
            }
            customWifiInfo.setSsid(ssid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customWifiInfo;
    }
}
