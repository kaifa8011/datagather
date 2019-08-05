package com.ciba.data.gather.listener;


import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.entity.DeviceData;
import com.ciba.data.synchronize.entity.ProcessData;

import java.util.List;

/**
 * @author ciba
 * @description 数据收集监听
 * @date 2018/12/3
 */
public interface DeviceDataGatherListener {
    void onDeviceDataGather(String crashData, DeviceData deviceData, List<CustomPackageInfo> installPackageList, List<ProcessData> appProcessList);
}
