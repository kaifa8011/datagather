package com.ciba.datagather.util.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ciba.datagather.constant.Constant;
import com.ciba.datagather.entity.CustomPhoneState;
import com.ciba.datagather.common.DataGatherManager;
import com.ciba.datagather.util.DataGatherLog;

import java.lang.reflect.Method;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : replace your description
 */

public class PhoneStateUtil {

    public static CustomPhoneState getPhoneState() {
        return getPhoneState(true);
    }

    @SuppressLint("HardwareIds")
    public static CustomPhoneState getPhoneState(boolean needDefaultValue) {
        CustomPhoneState customPhoneState = new CustomPhoneState();
        try {
            String androidId = Settings.Secure.getString(DataGatherManager.getInstance().getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            customPhoneState.setAndroidId(androidId);
            customPhoneState.setDeviceType(isPad() ? 5 : 4);

            int per = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.READ_PHONE_STATE);
            if (per == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
                String imsi = null;
                String iccid = null;
                String imei = null;
                boolean networkRoaming = false;
                if (tm != null) {
                    imsi = tm.getSubscriberId();
                    iccid = tm.getSimSerialNumber();
                    imei = tm.getDeviceId();
                    networkRoaming = tm.isNetworkRoaming();
                }
                if (needDefaultValue) {
                    customPhoneState.setImei(TextUtils.isEmpty(imei) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : imei);
                    customPhoneState.setIccid(TextUtils.isEmpty(iccid) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : iccid);
                    customPhoneState.setImsi(TextUtils.isEmpty(imsi) ? Constant.GET_DATA_FAILED_MAYBE_NO_SIM : imsi);
                } else {
                    customPhoneState.setImei(imei == null ? "" : imei);
                    customPhoneState.setIccid(iccid == null ? "" : iccid);
                    customPhoneState.setImsi(imsi == null ? "" : imsi);
                }
                customPhoneState.setIsNetworkRoaming(networkRoaming ? 1 : 0);
            } else if (needDefaultValue) {
                customPhoneState.setImei(Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION);
                customPhoneState.setIccid(Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION);
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

    private static void getImeiAndMeid(Context ctx, CustomPhoneState customPhoneState) {
        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (manager == null) {
            return;
        }
        try {
            Method method1 = manager.getClass().getMethod("getDeviceId", int.class);
            String deviceId = customPhoneState.getImei();
            if (deviceId != null && !Constant.GET_DATA_FAILED_MAYBE_NO_PERMISSION.equals(deviceId) && !Constant.GET_DATA_FAILED_MAYBE_NO_SIM.equals(deviceId)) {
                if (deviceId.length() == 14) {
                    // meid
                    customPhoneState.setMeid(deviceId);
                } else {
                    customPhoneState.setImei1(deviceId);
                }
            }
            String deviceId2 = (String) method1.invoke(manager, 1);
            if (deviceId2.length() == 14) {
                // meid
                customPhoneState.setMeid(deviceId2);
            } else {
                customPhoneState.setImei1(deviceId2);
            }
            String deviceId3 = (String) method1.invoke(manager, 2);
            if (deviceId3.length() == 14) {
                // meid
                customPhoneState.setMeid(deviceId3);
            } else if (!deviceId3.equals(customPhoneState.getImei1())) {
                customPhoneState.setImei2(deviceId3);
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
    }
}
