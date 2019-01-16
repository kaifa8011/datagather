package com.ciba.datagather.util.device;

import android.content.Context;
import android.text.TextUtils;

import com.ciba.datagather.common.DataGatherManager;
import com.ciba.datagather.util.DataGatherLog;

import java.io.FileReader;
import java.io.IOException;
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
        InputStreamReader inputStreamReader = null;
        LineNumberReader lineNumberReader = null;


        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            inputStreamReader = new InputStreamReader(pp.getInputStream());
            lineNumberReader = new LineNumberReader(inputStreamReader);
            for (; null != str; ) {
                str = lineNumberReader.readLine();
                if (str != null) {
                    // 去空格
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        } finally {
            if (lineNumberReader != null){
                try {
                    lineNumberReader.close();
                    lineNumberReader = null;
                } catch (IOException e) {
                    DataGatherLog.innerI(e.getMessage());
                }
            }
            if (inputStreamReader != null){
                try {
                    inputStreamReader.close();
                    inputStreamReader = null;
                } catch (IOException e) {
                    DataGatherLog.innerI(e.getMessage());
                }
            }
        }
        if (TextUtils.isEmpty(macSerial)) {
            macSerial = loadFileAsString("/sys/class/net/eth0/address");
        }
        return TextUtils.isEmpty(macSerial) ? getMacFromSecure(DataGatherManager.getInstance().getContext()) : macSerial;
    }

    private static String loadFileAsString(String fileName) {
        String text = null;
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
            text = loadReaderAsString(reader);
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
        }
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
            DataGatherLog.innerI(e.getMessage());
        }
        return "";
    }
}
