package com.ciba.data.gather.util;

import android.util.Log;

import com.ciba.datagather.BuildConfig;

/**
 * @author ciba
 * @description 描述
 * @date 2019/1/16
 */
public class DataGatherLog {
    private static final String TAG = "DataGatherInner";

    public static void innerI(String log) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, log + "");
        }
    }
}
