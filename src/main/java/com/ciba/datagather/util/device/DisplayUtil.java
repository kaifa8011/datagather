package com.ciba.datagather.util.device;

import android.content.res.Resources;

import com.ciba.datagather.common.DataGatherManager;

/**
 * @author ciba
 * @description 与屏幕显示相关的工具类
 * @date 2018/12/3
 */
public class DisplayUtil {
    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public static int dip2px(float dip) {
        return (int) (dip * getDensity() + 0.5f);
    }

    public static int px2dip(double px) {
        return (int) (px / getDensity() + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (sp * getScaledDensity() + 0.5f);
    }

    public static int px2sp(float px) {
        return (int) (px / getScaledDensity() + 0.5f);
    }

    public static float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity() {
        return getResources().getDisplayMetrics().scaledDensity;
    }

    public static float getDensityDpi() {
        return getResources().getDisplayMetrics().densityDpi;
    }

    public static Resources getResources(){
        return DataGatherManager.getInstance().getContext().getResources();
    }
}
