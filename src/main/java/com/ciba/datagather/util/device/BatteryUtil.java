package com.ciba.datagather.util.device;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;

import com.ciba.datagather.common.DataGatherManager;
import com.ciba.datagather.util.DataGatherLog;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/4
 */
public class BatteryUtil {
    /**
     * 获取剩余电量
     */
    public static int getBatteryLevel() {
        int level = 100;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BatteryManager manager = (BatteryManager) DataGatherManager.getInstance().getContext().getSystemService(Context.BATTERY_SERVICE);
                if (manager == null) {
                    return 100;
                }
                level = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return level;
    }
}
