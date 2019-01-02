package com.ciba.datagather.util.device;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author ciba
 * @description 描述
 * @date 2019/1/2
 */
public class TrafficUtil {
    /**
     * 通过uid查询文件夹中流量的数据适用于7.0及7.0之前的系统
     *
     * @param localUid
     * @return
     */
    public static long getTotalBytesManual(int localUid) {
        String textReceived = "0";
        String textSent = "0";
        BufferedReader brReceived = null;
        BufferedReader brSent = null;
        FileReader brReceivedFileReader = null;
        FileReader brFileSentReader = null;
        try {
            brReceivedFileReader = new FileReader("/proc/uid_stat/" + localUid + "/tcp_rcv");
            brReceived = new BufferedReader(brReceivedFileReader);

            brFileSentReader = new FileReader("/proc/uid_stat/" + localUid + "/tcp_snd");
            brSent = new BufferedReader(brFileSentReader);

            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (brReceivedFileReader != null) {
                    brReceivedFileReader.close();
                    brReceivedFileReader = null;
                }
                if (brFileSentReader != null) {
                    brFileSentReader.close();
                    brFileSentReader = null;
                }
                if (brReceived != null) {
                    brReceived.close();
                    brReceived = null;
                }
                if (brSent != null) {
                    brSent.close();
                    brSent = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return Long.parseLong(textReceived) + Long.parseLong(textSent);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
