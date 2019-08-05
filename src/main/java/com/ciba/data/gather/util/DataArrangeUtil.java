package com.ciba.data.gather.util;

import com.ciba.data.gather.listener.DeviceDataGatherListener;
import com.ciba.data.synchronize.common.DataSynchronizeManager;
import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.entity.DeviceData;
import com.ciba.data.synchronize.entity.ProcessData;

import java.util.List;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/6
 */
public class DataArrangeUtil {
    public static void dataGather(boolean withoutSystemApp, boolean appOnly, boolean geocoder, boolean getSignalStrengths) {
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
