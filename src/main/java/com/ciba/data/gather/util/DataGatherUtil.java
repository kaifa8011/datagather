package com.ciba.data.gather.util;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.entity.CustomBaseStation;
import com.ciba.data.gather.entity.CustomLocation;
import com.ciba.data.gather.entity.CustomPhoneState;
import com.ciba.data.gather.entity.CustomWifiInfo;
import com.ciba.data.gather.listener.DeviceDataGatherListener;
import com.ciba.data.gather.manager.OAIDManager;
import com.ciba.data.gather.manager.UniqueIdManager;
import com.ciba.data.gather.util.device.AdvertisingUtil;
import com.ciba.data.gather.util.device.BaseStationUtil;
import com.ciba.data.gather.util.device.BatteryUtil;
import com.ciba.data.gather.util.device.BlueToothUtil;
import com.ciba.data.gather.util.device.CpuUtil;
import com.ciba.data.gather.util.device.DisplayUtil;
import com.ciba.data.gather.util.device.LocationUtil;
import com.ciba.data.gather.util.device.NetworkUtil;
import com.ciba.data.gather.util.device.OtherDataUtil;
import com.ciba.data.gather.util.device.PackageUtil;
import com.ciba.data.gather.util.device.PhoneStateUtil;
import com.ciba.data.gather.util.device.ProcessUtil;
import com.ciba.data.gather.util.device.RootUtil;
import com.ciba.data.gather.util.device.WifiUtil;
import com.ciba.data.synchronize.entity.CustomBluetoothInfo;
import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.entity.DeviceData;
import com.ciba.data.synchronize.entity.ProcessData;
import com.ciba.data.synchronize.entity.WifiOtherDeviceData;
import com.ciba.data.synchronize.manager.DataCacheManager;
import com.ciba.http.manager.AsyncThreadPoolManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/3
 */
public class DataGatherUtil {

    /**
     * 手机设备信息
     *
     * @param withoutSystemApp         ：是否屏蔽系统应用（不搜集安装的系统应用信息）
     * @param appOnly                  ：是否每个应用只搜集一个进程信息
     * @param geocoder                 ：是否需要地理编码
     * @param deviceDataGatherListener ：设备信息收集监听
     */
    public static void gatherDeviceData(final boolean withoutSystemApp
            , final boolean appOnly
            , final boolean geocoder
            , boolean getSignalStrengths
            , final boolean isOnlyGetMachineId
            , final DeviceDataGatherListener deviceDataGatherListener) {
        if (deviceDataGatherListener == null) {
            return;
        }
        if (getSignalStrengths) {
            BaseStationUtil.getSignalStrengths(500, new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    final String signalStrength = (String) msg.obj;
                    gatherDeviceData(withoutSystemApp, appOnly, geocoder, signalStrength, isOnlyGetMachineId, deviceDataGatherListener);
                }
            });
        } else {
            gatherDeviceData(withoutSystemApp, appOnly, geocoder, Constant.GET_DATA_NULL, isOnlyGetMachineId, deviceDataGatherListener);
        }
    }

    private static void gatherDeviceData(final boolean withoutSystemApp, final boolean appOnly, final boolean geocoder
            , final String signalStrength, final boolean isOnlyGetMachineId, final DeviceDataGatherListener deviceDataGatherListener) {
        AsyncThreadPoolManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                CustomBaseStation customBaseStation = BaseStationUtil.getBaseStation(signalStrength);

                DeviceData deviceData = new DeviceData();

                gatherDisplayData(deviceData);

                gatherNetworkData(deviceData);

                gatherPhoneStateData(deviceData);

                gatherLocationAndStationData(geocoder, deviceData, customBaseStation);

                gatherBatteryData(deviceData);

                gatherBluetoothData(deviceData);

                gatherOtherData(deviceData);

                deviceData.setUqid(UniqueIdManager.getInstance(DataGatherManager.getInstance().getContext()).getUniqueId());

                deviceData.setOaid(OAIDManager.getInstance().getOAID());

                String crashData = DataCacheManager.getInstance().getCrashData();

                //若只是去获取machineId则不收集应用安装列表以及启动列表信息
                List<CustomPackageInfo> customPackageInfos = null;
                List<ProcessData> processDatas = null;
                if (!isOnlyGetMachineId) {
                    customPackageInfos = PackageUtil.getInstallPackageList(withoutSystemApp);
                    processDatas = ProcessUtil.getAppProcessList(withoutSystemApp, appOnly);
                }

                // getInstallPackageList()获取应用名称安装列表多时比较耗时
                deviceDataGatherListener.onDeviceDataGather(crashData
                        , deviceData
                        , customPackageInfos
                        , processDatas);
            }
        });
    }

    /**
     * 手机蓝牙信息
     */
    private static void gatherBluetoothData(DeviceData deviceData) {
        CustomBluetoothInfo bluetoothInfo = BlueToothUtil.getBluetoothInfo();
        deviceData.setBtmac(bluetoothInfo.getBluetoothDevice() == null ? "" : bluetoothInfo.getBluetoothDevice().getMac());
        deviceData.setBluetoothInfo(bluetoothInfo);
    }

    /**
     * 手机与电池相关的数据
     */
    private static void gatherBatteryData(DeviceData deviceData) {
        deviceData.setBattery(BatteryUtil.getBatteryLevel());
    }

    /**
     * 收集地理位置信息和基站信息
     */
    private static void gatherLocationAndStationData(boolean geocoder, DeviceData deviceData, CustomBaseStation customBaseStation) {
        final CustomLocation customLocation = LocationUtil.getCustomLocation(geocoder);
        deviceData.setLat(customLocation.getLat());
        deviceData.setLng(customLocation.getLng());
        deviceData.setCountry(customLocation.getCountry());
        deviceData.setCoordinateType(customLocation.getCoordinateType());
        deviceData.setLocaAccuracy(customLocation.getAccuracy());
        deviceData.setCoordTime(customLocation.getTime());

        if (customBaseStation != null) {
            deviceData.setBscid(customBaseStation.getBscid());
            deviceData.setBsss(customBaseStation.getBsss());
            deviceData.setLac(customBaseStation.getLac());
            deviceData.setCellularId(customBaseStation.getBscid());
            deviceData.setStbif(customBaseStation.getStbif() == null ? "" : customBaseStation.getStbif());
        }
    }

    /**
     * 收集其他数据
     */
    private static void gatherOtherData(DeviceData deviceData) {
        deviceData.setMachineType(1);
        deviceData.setUa(DataCacheManager.getInstance().getUserAgent());
        deviceData.setIdfa("");
        deviceData.setIdfv("");
        deviceData.setOpenUdid("");
        deviceData.setCid(OtherDataUtil.getCid());
        deviceData.setPdunid(OtherDataUtil.getUniquePsuedoID());
        deviceData.setCputy(CpuUtil.getCpuHardware());

        deviceData.setAdvertisingId(AdvertisingUtil.getGoogleAdId());
        if (DataGatherManager.getInstance().isCheckRoot()) {
            deviceData.setIsroot(RootUtil.checkRootPermission() ? 1 : 0);
        }
    }

    /**
     * 收集收集设备信息数据
     */
    private static void gatherPhoneStateData(DeviceData deviceData) {
        CustomPhoneState phoneState = PhoneStateUtil.getPhoneState();
        deviceData.setImsi(phoneState.getImsi());
        deviceData.setImei(phoneState.getImei());
        deviceData.setMeid(phoneState.getMeid());
        deviceData.setIccid(phoneState.getIccid());
        deviceData.setMcc(phoneState.getMcc());
        deviceData.setRoaming(phoneState.getIsNetworkRoaming());
        deviceData.setAndroidId(phoneState.getAndroidId());
        deviceData.setDeviceType(phoneState.getDeviceType());
    }

    /**
     * 收集网络相关数据
     */
    private static void gatherNetworkData(DeviceData deviceData) {
        deviceData.setNetworkType(NetworkUtil.getCurrentNetType(Constant.GET_DATA_NULL));
        deviceData.setIp(NetworkUtil.getHostIP());
        deviceData.setNetworkAddress(NetworkUtil.getMacAddress());

        CustomWifiInfo wifiInfo = WifiUtil.getWifiInfo();
        deviceData.setNetwkId(wifiInfo.getNetworkId());
        deviceData.setBssId(wifiInfo.getBssid());
        deviceData.setSsid(wifiInfo.getSsid());
        deviceData.setLksd(wifiInfo.getLinkSpeed());
        deviceData.setRssi(wifiInfo.getRssi());

        // 将其他连接WIFI的设备的mac地址以逗号分隔
        List<WifiOtherDeviceData> wifiOtherDeviceDataList = WifiUtil.datagramPacket(deviceData.getIp(), wifiInfo.getBssid());
        if (wifiOtherDeviceDataList != null && wifiOtherDeviceDataList.size() > 0) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < wifiOtherDeviceDataList.size(); i++) {
                    WifiOtherDeviceData wifiOtherDeviceData = wifiOtherDeviceDataList.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ip", wifiOtherDeviceData.getIp());
                    jsonObject.put("mac", wifiOtherDeviceData.getMac());
                    jsonArray.put(jsonObject);
                }
                wifiOtherDeviceDataList.clear();
                deviceData.setNd(jsonArray.toString());
                jsonArray = null;
            } catch (Exception e) {
                DataGatherLog.innerI(e.getMessage());
            }
        }
    }

    /**
     * 收集展示信息（类似屏幕宽高、分辨率等）
     */
    private static void gatherDisplayData(DeviceData deviceData) {
        deviceData.setScreenWidth(DisplayUtil.getScreenWidth());
        deviceData.setScreenHeight(DisplayUtil.getScreenHeight());
        deviceData.setSdScreendpi(String.valueOf(DisplayUtil.getDensityDpi()));
        deviceData.setOsVersion(Build.VERSION.RELEASE);
        deviceData.setVendor(Build.MANUFACTURER);
        deviceData.setModelNo(Build.MODEL);
        deviceData.setWordSize(DisplayUtil.getFontOrUiScale() + "");
    }
}