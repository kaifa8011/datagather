package com.ciba.data.gather.util.device;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 小米设备工具类，用来获取小米系统信息
 *
 * @author songzi
 * @date 2021/4/15
 */
public class XiaoMiDeviceUtil {


    /**
     * 判断是否为小米手机
     *
     * @return
     */
    public static boolean checkMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if (TextUtils.isEmpty(manufacturer)) {
            return false;
        }
        if (!manufacturer.equals("Xiaomi")) {
            return false;
        }
        return true;
    }


    /**
     * 判断是否为小米版本小于v11
     *
     * @return
     */
    public static boolean checkMIUIVersionNameLtV10() {
        try {
            String versionName = getSystemProperty("ro.miui.ui.version.name");
            //v125 = MIUI12.5
            if (!TextUtils.isEmpty(versionName) && versionName.startsWith("V")) {
                versionName = versionName.substring(1);
                if (!TextUtils.isEmpty(versionName)) {
                    Integer version = Integer.valueOf(versionName);
                    return version < 11;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSystemProperty(String propName) {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


}
