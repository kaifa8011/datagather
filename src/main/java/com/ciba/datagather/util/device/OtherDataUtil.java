package com.ciba.datagather.util.device;

import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.UUID;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/4
 */
public class OtherDataUtil {
    /**
     * 获取cid
     */
    public static String getCid() {
        String str1 = null;
        Object localOb;
        try {
            localOb = new FileReader("/sys/block/mmcblk0/device/type");
            localOb = new BufferedReader((Reader) localOb).readLine()
                    .toLowerCase().contentEquals("mmc");
            if (localOb != null) {
                str1 = "/sys/block/mmcblk0/device/";
            }
            // nand ID
            localOb = new FileReader(str1 + "cid");
            str1 = new BufferedReader((Reader) localOb).readLine();
        } catch (Exception e1) {
        }
        return str1;
    }

    public static String getUniquePsuedoID() {
        String serial = null;
        String deviceId = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(deviceId.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            //serial需要一个初始化
            // 随便一个初始化
            serial = "serial";
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(deviceId.hashCode(), serial.hashCode()).toString();
    }
}
