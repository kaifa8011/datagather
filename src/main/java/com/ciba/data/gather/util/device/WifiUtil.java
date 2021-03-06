package com.ciba.data.gather.util.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.entity.CustomWifiInfo;
import com.ciba.data.gather.util.DataGatherLog;
import com.ciba.data.synchronize.entity.WifiOtherDeviceData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : 获取WiFi数据的工具类
 */

public class WifiUtil {

    private static final int TIME_OUT = 20;
    private static boolean canGetWifiInfo = true;

    /**
     * 在权限允许下是否可以获取wifi信息
     *
     * @param canGetWifiInfo
     */
    public static void setCanGetWifiInfo(boolean canGetWifiInfo) {
        WifiUtil.canGetWifiInfo = canGetWifiInfo;
    }

    public static boolean isCanGetWifiInfo() {
        return canGetWifiInfo;
    }

    /**
     * 獲取WIFi信息
     *
     * @return
     */
    public static CustomWifiInfo getWifiInfo() {
        CustomWifiInfo customWifiInfo = new CustomWifiInfo();
        try {
            int permission = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.ACCESS_WIFI_STATE);
            if (!canGetWifiInfo || permission != PackageManager.PERMISSION_GRANTED) {
                return customWifiInfo;
            }
            WifiManager wifiManager = (WifiManager) DataGatherManager.getInstance().getContext().getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return customWifiInfo;
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                return customWifiInfo;
            }

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
            DataGatherLog.innerI(e.getMessage());
        }
        return customWifiInfo;
    }

    /**
     * 通过当前Ip，发送消息包（系统会将响应的信息存入/proc/net/arp）
     *
     * @param localIp ：当前ip
     * @param bssid   ：当前WiFi，主要用于判断是否是WiFi状态
     * @return
     */
    public static List<WifiOtherDeviceData> datagramPacket(String localIp, String bssid) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return null;
        }
        if (TextUtils.isEmpty(localIp) || TextUtils.isEmpty(bssid)) {
            return null;
        }

        DatagramSocket socket = null;
        try {
            DatagramPacket dp = new DatagramPacket(new byte[0], 0, 0);
            socket = new DatagramSocket();
            socket.setSoTimeout(TIME_OUT);
            int position = 2;
            String netIp = localIp.substring(0, localIp.lastIndexOf(".") + 1);
            while (position < 255) {
                dp.setAddress(InetAddress.getByName(netIp + position));
                socket.send(dp);
                position++;
                if (position % 100 == 0) {
                    //分三段掉包，一次性发的话，达到236左右，会耗时3秒左右再往下发
                    socket.close();
                    socket = new DatagramSocket();
                    socket.setSoTimeout(TIME_OUT);
                }
            }
        } catch (IOException e) {
            DataGatherLog.innerI(e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        }

        return getSameWifiOtherDeviceData();
    }

    /**
     * 读取/proc/net/arp文件获取同一WiFi下其他设备信息
     *
     * @return
     */
    public static List<WifiOtherDeviceData> getSameWifiOtherDeviceData() {
        List<WifiOtherDeviceData> wifiOtherDeviceDataList = new ArrayList<>();
        BufferedReader reader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("/proc/net/arp");
            reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            //读取第一行信息，就是IP address HW type Flags HW address Mask Device
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                if (tokens.length < 6) {
                    continue;
                }
                String mac = tokens[3];
                if ("00:00:00:00:00:00".equals(mac)) {
                    continue;
                }
                wifiOtherDeviceDataList.add(new WifiOtherDeviceData(tokens[0], mac, tokens[2]));
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    DataGatherLog.innerI(e.getMessage());
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                    fileReader = null;
                } catch (IOException e) {
                    DataGatherLog.innerI(e.getMessage());
                }
            }
        }
        return wifiOtherDeviceDataList;
    }


    /**
     * 获取周边路由设备信息(需要位置权限)
     */
    public static List<CustomWifiInfo> getPeripheralWifiData() {
        List<CustomWifiInfo> infos = new ArrayList<>();
        try {
            int permission = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(),
                    Manifest.permission.ACCESS_WIFI_STATE);
            if (!canGetWifiInfo || permission != PackageManager.PERMISSION_GRANTED) {
                return infos;
            }
            WifiManager wifiManager = (WifiManager) DataGatherManager.getInstance().getContext()
                    .getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return null;
            }
            ArrayList<ScanResult> list = (ArrayList<ScanResult>) wifiManager.getScanResults();
            if (list == null) {
                return null;
            }
            CustomWifiInfo info;
            for (ScanResult result : list) {
                info = new CustomWifiInfo();
                info.setSsid(result.SSID);
                info.setBssid(result.BSSID);
                info.setRssi(result.level);
                infos.add(info);
            }
        } catch (Exception e) {
        }
        return infos;
    }

}
