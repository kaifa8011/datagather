package com.ciba.data.gather.util.device;

/**
 * @Description: 禁止设备读取信息工具类
 * @Author: maipian
 * @CreateDate: 2021/1/5 9:12 AM
 */
public class DeviceUnableReadUtil {
    public static boolean isUseImei() {
        if ("1".equals(PropertyUtils.get("ro.miui.restrict_imei_p","0"))) {
            return false;
        }
        return true;
    }

    public static boolean isUseIMSI() {
        if ("1".equals(PropertyUtils.get("ro.miui.restrict_imei_p","0"))) {
            return false;
        }
        return true;
    }

    public static boolean isUseICCID() {
        if ("1".equals(PropertyUtils.get("ro.miui.restrict_imei_p","0"))) {
            return false;
        }
        return true;
    }

    public static boolean isUseSerial() {
        if ("1".equals(PropertyUtils.get("ro.miui.restrict_imei_p","0"))) {
            return false;
        }
        return true;
    }
}
