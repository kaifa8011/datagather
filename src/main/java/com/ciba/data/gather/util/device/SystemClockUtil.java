package com.ciba.data.gather.util.device;

import android.os.SystemClock;

/**
 * @author songzi
 * @description 系统时钟相关的工具类
 * @date 2021/2/18
 */
public class SystemClockUtil {

    /**
     * 返回的是系统从启动到现在的时间,单位:ms
     * @return
     */
    public static long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * 返回开机时刻的时间戳,单位:ms
     * @return
     */
    public static long getUpTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

}
