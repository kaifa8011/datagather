package com.ciba.datagather.util;

import android.util.Log;

/**
 * @author ciba
 * @description 描述
 * @date 2019/1/16
 */
public class DataGatherLog {
    private static final String TAG = "DataGatherInner";

    public static void innerI(String log) {
        Log.i(TAG, log + "");
    }
}
