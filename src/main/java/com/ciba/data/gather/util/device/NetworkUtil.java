package com.ciba.data.gather.util.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.util.DataGatherLog;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {
    private static final String DEFAULT_MAC = "02:00:00:00:00:00";

    public static boolean networkAvaliable() {
        try {
            final ConnectivityManager manager = (ConnectivityManager) DataGatherManager.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo info = manager.getActiveNetworkInfo();
            if (null == info) {
                return false;
            }
            return info.isAvailable();
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return false;
    }

    public static boolean isWifi() {
        try {
            WifiManager wm = (WifiManager) DataGatherManager.getInstance().getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return (wm != null && WifiManager.WIFI_STATE_ENABLED == wm.getWifiState());
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return false;
    }

    /**
     * 获取当前网络类型
     */
    public static String getCurrentNetType() {
        return getCurrentNetType(Constant.NETWORK_DEFAULT_TYPE);
    }

    /**
     * 获取网络类型
     *
     * @param defaultType ：默认类型
     */
    public static String getCurrentNetType(String defaultType) {
        String type = defaultType;
        try {
            ConnectivityManager cm = (ConnectivityManager) DataGatherManager.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                type = defaultType;
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                type = "WIFI";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subType = info.getSubtype();
                if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                        || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                    type = "2G";
                } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                    type = "3G";
                } else if (subType == 13) {
                    // LTE 13是3g到4g的过渡，是3.9G的全球标准
                    type = "4G";
                }
            }
        } catch (Exception e){
            DataGatherLog.innerI(e.getMessage());
        }
        return type;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return hostIp;
    }

    public static String getMacAddress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getMacAddressAboveVersionM();
        }
        return getMacAddressBelowVersionM();
    }

    public static String getMacAddressBelowVersionM() {
        try {
            WifiManager wifi = (WifiManager) DataGatherManager.getInstance().getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi == null) {
                return DEFAULT_MAC;
            }
            WifiInfo info = wifi.getConnectionInfo();
            if (info == null) {
                return DEFAULT_MAC;
            }
            @SuppressLint("HardwareIds")
            String mac = info.getMacAddress();
            if (TextUtils.isEmpty(mac)) {
                mac = DEFAULT_MAC;
            }
            return mac;
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return DEFAULT_MAC;
    }

    /**
     * 获取Mac地址 7.0
     */

    public static String getMacAddressAboveVersionM() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return DEFAULT_MAC;
    }
}
