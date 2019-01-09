# Datagather

Datagather 是一个较全面的Android设备信息收集库，可以帮助开发者快速集成设备信息收集功能。



# 怎么使用

* Step1：Gradle中添加依赖：

```
dependencies {
    compile 'com.ciba:datagather:0.1.6'
}
```

* Step2：在Application中进行初始化

```
DataGatherManager.getInstance().init(this);
```

* Step3：设备信息获取

```
/**
 * 手机设备信息
 *
 * @param withoutSystemApp         ：是否屏蔽系统应用（不搜集安装的系统应用信息）
 * @param appOnly                  ：是否每个应用只搜集一个进程信息
 * @param geocoder                 ：是否需要地理编码
 * @param deviceDataGatherListener ：设备信息收集监听
 */
DataGatherUtil.gatherDeviceData(withoutSystemApp, appOnly, geocoder, getSignalStrengths, new DeviceDataGatherListener() {
            @Override
            public void onDeviceDataGather(String crashData
                    , DeviceData deviceData
                    , List<CustomPackageInfo> installPackageList
                    , List<ProcessData> appProcessList) {
                // do something
            }
        });
```