package com.ciba.datagather.util.device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import com.ciba.datagather.util.DataGatherLog;
import com.ciba.datasynchronize.entity.CustomBluetoothInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author : ciba
 * @date : 2018/7/23
 * @description : 蓝牙数据获取工具类
 */

public class BlueToothUtil {

    /**
     * 获取蓝牙的MAC地址
     */
    public static String getMac() {
        return "";
    }

    @SuppressLint("MissingPermission")
    public static CustomBluetoothInfo getBluetoothInfo() {
        CustomBluetoothInfo bluetoothInfo = new CustomBluetoothInfo();
        CustomBluetoothInfo.CustomBluetoothDevice bluetoothDevice = new CustomBluetoothInfo.CustomBluetoothDevice();
        String bluetoothMacAddress = null;
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                return bluetoothInfo;
            }
            bluetoothDevice.setName(bluetoothAdapter.getName());
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            List<CustomBluetoothInfo.CustomBluetoothDevice> bluetoothDeviceList = new ArrayList<>();
            if (bondedDevices != null && bondedDevices.size() > 0) {
                for (BluetoothDevice device : bondedDevices) {
                    CustomBluetoothInfo.CustomBluetoothDevice bondedBluetoothDevice = new CustomBluetoothInfo.CustomBluetoothDevice();
                    bondedBluetoothDevice.setName(device.getName());
                    bondedBluetoothDevice.setMac(device.getAddress());
                    bondedBluetoothDevice.setType(getDeviceString(device.getBluetoothClass().getDeviceClass()));
                    bluetoothDeviceList.add(bondedBluetoothDevice);
                }
            }
            bluetoothInfo.setBondedDevices(bluetoothDeviceList);

            // 以下代码是通过反射获取自己设备的蓝牙Mac地址，次反射方法在8.1及以上不在有效
            Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
            mServiceField.setAccessible(true);

            Object btManagerService = mServiceField.get(bluetoothAdapter);

            if (btManagerService != null) {
                bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
            }
        } catch (Exception e) {
            DataGatherLog.innerI("getBluetoothAddress: " + e.getMessage());
        }
        bluetoothDevice.setMac(bluetoothMacAddress);
        bluetoothInfo.setBluetoothDevice(bluetoothDevice);
        return bluetoothInfo;
    }

    private static String getDeviceString(int deviceType) {
        String name;
        switch (deviceType) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                name = "摄像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                name = "车载设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                name = "蓝牙耳机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                name = "扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                name = "麦克风";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                name = "便携音频设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                name = "机顶盒";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                name = "耳机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                name = "未分类音视频设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                name = "VCR";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                name = "摄影机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                name = "视频会议";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                name = "显示器和扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                name = "视频游戏设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                name = "显示器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                name = "可穿戴式耳机";
                break;
            case BluetoothClass.Device.PHONE_CELLULAR:
                name = "手机";
                break;
            case BluetoothClass.Device.PHONE_CORDLESS:
                name = "无线电设备";
                break;
            case BluetoothClass.Device.PHONE_ISDN:
                name = "手机服务数据网";
                break;
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                name = "手机调制解调器或网关";
                break;
            case BluetoothClass.Device.PHONE_SMART:
                name = "智能手机";
                break;
            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                name = "未分类的手机";
                break;
            case BluetoothClass.Device.WEARABLE_GLASSES:
                name = "可穿戴眼睛";
                break;
            case BluetoothClass.Device.WEARABLE_HELMET:
                name = "可穿戴头盔";
                break;
            case BluetoothClass.Device.WEARABLE_JACKET:
                name = "可穿戴上衣";
                break;
            case BluetoothClass.Device.WEARABLE_PAGER:
                name = "可穿戴寻呼机";
                break;
            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                name = "未分类的可穿戴设备";
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                name = "可穿戴手表";
                break;
            case BluetoothClass.Device.TOY_CONTROLLER:
                name = "可控玩具";
                break;
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                name = "动作人偶玩具娃娃";
                break;
            case BluetoothClass.Device.TOY_GAME:
                name = "游戏玩具";
                break;
            case BluetoothClass.Device.TOY_ROBOT:
                name = "玩具机器人";
                break;
            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                name = "未分类的玩具设备";
                break;
            case BluetoothClass.Device.TOY_VEHICLE:
                name = "玩具车";
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                name = "健康状态-血压";
                break;
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                name = "健康状态数据";
                break;
            case BluetoothClass.Device.HEALTH_GLUCOSE:
                name = "健康状态葡萄糖";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                name = "健康状态脉搏血氧计";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                name = "健康状态脉搏速率";
                break;
            case BluetoothClass.Device.HEALTH_THERMOMETER:
                name = "健康状态体温计";
                break;
            case BluetoothClass.Device.HEALTH_WEIGHING:
                name = "健康状态体重";
                break;
            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                name = "未知健康状态设备";
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                name = "电脑桌面";
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                name = "手提电脑或Pad";
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                name = "便携式电脑";
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                name = "微型电脑";
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                name = "电脑服务器";
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                name = "未分类的电脑设备";
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                name = "穿戴式电脑";
                break;
            default:
                name = "未知设备类型";
                break;
        }
        return name;
    }
}
