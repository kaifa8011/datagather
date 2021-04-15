package com.ciba.data.gather.util;

import com.ciba.data.gather.listener.DeviceDataGatherListener;
import com.ciba.data.synchronize.OnDeviceDataUpLoadListener;
import com.ciba.data.synchronize.common.DataSynchronizeManager;
import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.entity.DeviceData;
import com.ciba.data.synchronize.entity.ProcessData;

import java.util.List;

/**
 * @author ciba
 * @description 数据整理工具类
 * @date 2018/12/6
 */
public class DataArrangeUtil {
    /**
     * 数据收集
     * @param withoutSystemApp 是否屏蔽系统应用（不搜集安装的系统应用信息）
     * @param appOnly 是否每个应用只搜集一个进程信息
     * @param geocoder 是否需要地理编码
     * @param getSignalStrengths 是否获取信号强度
     * @param upLoadListener 上报监听
     * @param isOnlyGetMachineId 是否只获取machineID
     */
    public static void dataGather(boolean withoutSystemApp, boolean appOnly, boolean geocoder, boolean getSignalStrengths, final OnDeviceDataUpLoadListener upLoadListener, final boolean isOnlyGetMachineId) {
        DataGatherUtil.gatherDeviceData(withoutSystemApp, appOnly, geocoder, getSignalStrengths, isOnlyGetMachineId, new DeviceDataGatherListener() {
            @Override
            public void onDeviceDataGather(String crashData
                    , DeviceData deviceData
                    , List<CustomPackageInfo> installPackageList
                    , List<ProcessData> appProcessList) {
                arrangeData(crashData, deviceData, installPackageList, appProcessList, upLoadListener, isOnlyGetMachineId);
            }
        });
    }

    /**
     *
     * @param crashData 异常上报信息
     * @param deviceData 设备信息
     * @param installPackageList 安装列表集合
     * @param appProcessList 运行列表集合
     * @param upLoadListener 设备数据上报监听
     * @param isOnlyGetMachineId 是否只获取machineID
     */
    public static void arrangeData(String crashData
            , DeviceData deviceData
            , List<CustomPackageInfo> installPackageList
            , List<ProcessData> appProcessList
            , OnDeviceDataUpLoadListener upLoadListener
            , boolean isOnlyGetMachineId) {
        DataSynchronizeManager.getInstance().getDataGatherListener().onDataGather(crashData, deviceData
                , installPackageList, appProcessList, upLoadListener, isOnlyGetMachineId);
    }
}
