package com.ciba.data.gather.util;

import com.ciba.datagather.BuildConfig;

/**
 * @author parting_soul
 * @date 2019-09-20
 */
public class ExceptionUtils {
    public static void printStackTrace(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }
}
