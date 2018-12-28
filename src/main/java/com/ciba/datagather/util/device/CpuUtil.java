package com.ciba.datagather.util.device;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author : ciba
 * @date : 2018/7/23
 * @description : 获取CPU信息工具类
 */

public class CpuUtil {
    /**
     * 获取CPU型号，好像不一定能获取到
     */
    public static String getCpuHardware() {
        String cpuHardware = "";
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
             fr = new FileReader("/proc/cpuinfo");
             localBufferedReader = new BufferedReader(fr, 8192);

            //查找CPU型号
            for (int i = 1; i < 100; i++) {
                String cupInfo = localBufferedReader.readLine();
                if (cupInfo == null) {
                    break;
                } else if (cupInfo.contains("Hardware")) {
                    String[] splits = cupInfo.split(":");
                    if (splits.length > 1) {
                        cpuHardware = splits[1].trim();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (localBufferedReader != null){
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cpuHardware;
    }

}
