package com.ciba.datagather.listener;

import com.ciba.datasynchronize.entity.CustomPackageInfo;
import com.ciba.datasynchronize.entity.DeviceData;
import com.ciba.datasynchronize.entity.ProcessData;

import java.util.List;

/**
 * @author ciba
 * @description 数据收集监听
 * @date 2018/12/3
 */
public interface DeviceDataGatherListener {
    void onDeviceDataGather(String crashData, DeviceData deviceData, List<CustomPackageInfo> installPackageList, List<ProcessData> appProcessList);
}
