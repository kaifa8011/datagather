package com.ciba.datagather.util;

import com.ciba.datasynchronize.entity.CustomPackageInfo;
import com.ciba.datasynchronize.entity.DeviceData;
import com.ciba.datagather.listener.DeviceDataGatherListener;
import com.ciba.datasynchronize.entity.ProcessData;
import com.ciba.datasynchronize.common.DataSynchronizeManager;

import java.util.List;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/6
 */
public class DataArrangeUtil {
    private static long millis;

    public static void dataGather(boolean withoutSystemApp, boolean appOnly, boolean geocoder, boolean getSignalStrengths) {
        millis = System.currentTimeMillis();
        DataGatherUtil.gatherDeviceData(withoutSystemApp, appOnly, geocoder, getSignalStrengths, new DeviceDataGatherListener() {
            @Override
            public void onDeviceDataGather(String crashData
                    , DeviceData deviceData
                    , List<CustomPackageInfo> installPackageList
                    , List<ProcessData> appProcessList) {
                arrangeData(crashData, deviceData, installPackageList, appProcessList);
            }
        });
    }

    public static void arrangeData(String crashData
            , DeviceData deviceData
            , List<CustomPackageInfo> installPackageList
            , List<ProcessData> appProcessList) {
        DataSynchronizeManager.getInstance().getDataGatherListener().onDataGather(crashData, deviceData
                , installPackageList, appProcessList);
    }
}
