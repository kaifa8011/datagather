package com.ciba.datagather.manager;

import android.text.TextUtils;

import com.ciba.datagather.entity.CustomPhoneState;
import com.ciba.datagather.util.MD5Util;
import com.ciba.datagather.util.device.NetworkUtil;
import com.ciba.datagather.util.device.PhoneStateUtil;
import com.ciba.datasynchronize.util.SPUtil;

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
        if (mac != null) {
            if (deviceId != null) {
                id = mac + deviceId;
            } else {
                id = mac;
            }
        } else if (imei != null) {
            if (deviceId != null) {
                id = imei + deviceId;
            } else {
                id = imei;
            }
        } else {
            id = getUUID();
        }
        id = MD5Util.getMD5Code(id);
        return id;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}