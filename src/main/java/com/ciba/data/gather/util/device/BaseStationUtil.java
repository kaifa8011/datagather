package com.ciba.data.gather.util.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.entity.CustomBaseStation;
import com.ciba.data.gather.listener.CustomPhoneStateListener;
import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.util.DataGatherLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : 基站信息获取工具类
 */

public class BaseStationUtil {
    /**
     * 手机信号强度信息
     *
     * @param handler：通过handler返回收集的字符串
     */
    public static void getSignalStrengths(long time, Handler handler) {
//        try {
//            TelephonyManager telephonyManager = (TelephonyManager)
//                    DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
//            CustomPhoneStateListener phoneStateListener = new CustomPhoneStateListener(handler);
//            if (telephonyManager == null) {
//                phoneStateListener.sentMessage(Constant.GET_DATA_FAILED_MAYBE_NO_SIM);
//                return;
//            }
//            int simState = telephonyManager.getSimState();
//            if (TelephonyManager.SIM_STATE_ABSENT == simState || TelephonyManager.SIM_STATE_UNKNOWN == simState) {
//                phoneStateListener.sentMessage(Constant.GET_DATA_FAILED_MAYBE_NO_SIM);
//                return;
//            }
//            phoneStateListener.sentMessageDelayed(time);
//            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//        } catch (Exception e) {
//            DataGatherLog.innerI(e.getMessage());
//        }
    }

    /**
     * 获取基站id
     *
     * @param signalStrength : 手机信号强度信息，如果不需要刻意不传，可以通过getSignalStrengths方法获取
     */
    public static CustomBaseStation getBaseStation(String signalStrength) {
        final CustomBaseStation customBaseStation = new CustomBaseStation();
        return customBaseStation;
    }
}
