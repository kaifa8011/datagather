package com.ciba.datagather.util.device;

import android.content.Context;
import android.text.TextUtils;

import com.ciba.datagather.common.DataGatherManager;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;


/**
 * @author : ciba
 * @date : 2018/7/23
 * @description : 蓝牙数据获取工具类
 */

public class BlueToothUtil {

    /**
     * 获取蓝牙的MAC地址
     */
    public static String getMac() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    // 去空格
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(macSerial)) {
            try {
                macSerial = loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TextUtils.isEmpty(macSerial) ? getMacFromSecure(DataGatherManager.getInstance().getContext()) : macSerial;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    private static String getMacFromSecure(Context context) {
        try {
            return android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
