package com.ciba.data.gather.manager;

import android.os.Build;
import android.text.TextUtils;

import com.ciba.data.gather.entity.CustomPhoneState;
import com.ciba.data.gather.util.MD5Util;
import com.ciba.data.gather.util.device.NetworkUtil;
import com.ciba.data.gather.util.device.PhoneStateUtil;
import com.ciba.data.synchronize.util.SPUtil;

import java.util.UUID;

/**
 * @author : ciba
 * @date : 2018/7/9
 * @description : 获取安卓设备唯一标识码
 */

public class MachineManager {
    private static final String MACHINE_ID = "GATHER_MACHINE_ID";
    private static MachineManager instance;
    private String machine;

    public MachineManager() {
    }

    public static MachineManager getInstance() {
        if (instance == null) {
            synchronized (MachineManager.class) {
                if (instance == null) {
                    instance = new MachineManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Machine
     */
    public String getMachine() {
        // 如果内存中的值不为空，则获取内存中的值
        if (!TextUtils.isEmpty(machine)) {
            return machine;
        }
        // 如果内存中的值为空，则先获取SP持久化的值
        machine = SPUtil.getString(MACHINE_ID);
        if (!TextUtils.isEmpty(machine)) {
            return machine;
        }
        // 如果本地持久化都为空，则获取新的值并本地持久化
        machine = getNewMachine();
        SPUtil.putString(MACHINE_ID, machine);
        return machine;
    }

    /**
     * 获取新的Machine值
     *
     * @return
     */
    public String getNewMachine() {
        CustomPhoneState phoneState = PhoneStateUtil.getPhoneState(false);

        String imei = phoneState.getRealImei();
        String mac = NetworkUtil.getMacAddress();
        String deviceId = phoneState.getAndroidId();

        String id;
        if (!TextUtils.isEmpty(mac) && Build.VERSION.SDK_INT <= 28) {
            if (!TextUtils.isEmpty(deviceId)) {
                id = mac + deviceId;
            } else {
                id = mac;
            }
        } else if (!TextUtils.isEmpty(imei)) {
            if (!TextUtils.isEmpty(deviceId)) {
                id = imei + deviceId;
            } else {
                id = imei;
            }
        } else if (!TextUtils.isEmpty(deviceId)) {
            id = deviceId;
        } else {
            id = getUUID();
        }
        return MD5Util.getMD5Code(id);
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}