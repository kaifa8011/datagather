package com.ciba.data.gather.util.device;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.util.DataGatherLog;

import java.lang.reflect.Method;

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

    public static Resources getResources() {
        return DataGatherManager.getInstance().getContext().getResources();
    }

    public static float getFontOrUiScale() {
        float scale = 1f;
        try {
            scale = getResources().getConfiguration().fontScale;
            if (scale == 1) {
                float densityDpi = getDensityDpi();
                int deviceDensity = getDeviceDensity();
                if (densityDpi > 0 && deviceDensity > 0) {
                    scale = densityDpi / deviceDensity;
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage() + "");
        }
        return scale;
    }

    public static int getDeviceDensity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return DisplayMetrics.DENSITY_DEVICE_STABLE;
        }
        try {
            Class<?> cls = Class.forName("android.util.DisplayMetrics");
            Method method = cls.getDeclaredMethod("getDeviceDensity");
            method.setAccessible(true);
            return (Integer) method.invoke(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取手机屏幕亮度，正常情况下是1-255，个别品牌手机阈值不能确定
     * @param context
     * @return
     */
    public static int getScreenBrightness(Context context) {
        int value = 0;
        ContentResolver cr = context.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {

        }
        return value;
    }
}
