package com.ciba.data.gather.util.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.entity.CustomPhoneState;
import com.ciba.data.gather.util.DataGatherLog;

import java.lang.reflect.Method;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : replace your description
 */

public class PhoneStateUtil {
    private static boolean canGetPhoneStateInfo = true;

    /**
     * 在权限允许下是否可以获取设备信息
     *
     * @param canGetPhoneStateInfo
     */
    public static void setCanGetPhoneStateInfo(boolean canGetPhoneStateInfo) {
        PhoneStateUtil.canGetPhoneStateInfo = canGetPhoneStateInfo;
    }

    public static boolean isCanGetPhoneStateInfo() {
        return canGetPhoneStateInfo;
    }

    public static CustomPhoneState getPhoneState() {
        return getPhoneState(true);
    }

    @SuppressLint("HardwareIds")
    public static CustomPhoneState getPhoneState(boolean needDefaultValue) {
        CustomPhoneState customPhoneState = new CustomPhoneState();

        // 隐私协议拒绝的情况下，不读取信息
        if (!canGetPhoneStateInfo) {
            return customPhoneState;
        }

        try {
            String androidId = Settings.Secure.getString(DataGatherManager.getInstance().getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(androidId)) {
                try {
                    androidId = Settings.System.getString(DataGatherManager.getInstance().getContext().getContentResolver(), Settings.System.ANDROID_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            customPhoneState.setAndroidId(androidId);
            customPhoneState.setDeviceType(isPad() ? 5 : 4);

            int per = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.READ_PHONE_STATE);
            if (per == PackageManager.PERMISSION_GRANTED) {

                String imsi = null;
//                String iccid = null;
                String imei = null;
                boolean networkRoaming = false;
                if (DeviceUnableReadUtil.isUseImei()) {
                    TelephonyManager tm ;
                    tm = (TelephonyManager) DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (tm != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                imei = tm.getImei();
                            } catch (Exception e) {
                                DataGatherLog.innerI(e.getMessage());
                            }
                        }
                        imsi = tm.getSubscriberId();
//                        iccid = tm.getSimSerialNumber();
                        if (TextUtils.isEmpty(imei)) {
                            try {
                                imei = tm.getDeviceId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        networkRoaming = tm.isNetworkRoaming();
                    }
                }

                if (needDefaultValue) {
                    customPhoneState.setImei(TextUtils.isEmpty(imei) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : imei);
//                    customPhoneState.setIccid(TextUtils.isEmpty(iccid) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : iccid);
                    customPhoneState.setImsi(TextUtils.isEmpty(imsi) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : imsi);
                } else {
                    customPhoneState.setImei(imei == null ? "" : imei);
//                    customPhoneState.setIccid(iccid == null ? "" : iccid);
                    customPhoneState.setImsi(imsi == null ? "" : imsi);
                }
                customPhoneState.setIsNetworkRoaming(networkRoaming ? 1 : 0);
            } else if (needDefaultValue) {
                customPhoneState.setImei(Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION);
//                customPhoneState.setIccid(Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION);
                customPhoneState.setImsi(Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION);
            }
            getImeiAndMeid(DataGatherManager.getInstance().getContext(), customPhoneState);
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return customPhoneState;
    }

    private static boolean isPad() {
        return (DataGatherManager.getInstance().getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @SuppressLint("MissingPermission")
    private static void getImeiAndMeid(Context ctx, CustomPhoneState customPhoneState) {
        TelephonyManager manager = null;
        try {
            manager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager == null) {
                return;
            }

            Method method1 = manager.getClass().getMethod("getDeviceId", int.class);
            String deviceId = customPhoneState.getImei();
            if (deviceId != null && !Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION.equals(deviceId) && !Constant.GET_DATA_FAILED_MAYBE_NO_SIM.equals(deviceId)) {
                if (deviceId.length() == 14) {
                    // meid
                } else {
                    customPhoneState.setImei1(deviceId);
                }
            }
            String deviceId2 = (String) method1.invoke(manager, 1);
            if (deviceId2.length() == 14) {
                // meid
            } else {
                customPhoneState.setImei1(deviceId2);
            }
            String deviceId3 = (String) method1.invoke(manager, 2);
            if (deviceId3.length() == 14) {
                // meid
            } else if (!deviceId3.equals(customPhoneState.getImei1())) {
                customPhoneState.setImei2(deviceId3);
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        if (manager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String imei1 = manager.getImei(0);
                String imei2 = manager.getImei(1);
                if (!TextUtils.isEmpty(imei1)) {
                    customPhoneState.setImei1(imei1);
                }
                if (!TextUtils.isEmpty(imei2)) {
                    customPhoneState.setImei2(imei2);
                }

            } catch (Exception e) {
                DataGatherLog.innerI(e.getMessage());
            }
        }
    }
}
