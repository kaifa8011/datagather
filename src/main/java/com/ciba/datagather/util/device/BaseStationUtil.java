package com.ciba.datagather.util.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.ciba.datagather.constant.Constant;
import com.ciba.datagather.entity.CustomBaseStation;
import com.ciba.datagather.listener.CustomPhoneStateListener;
import com.ciba.datagather.common.DataGatherManager;

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
    public static void getSignalStrengths(final Handler handler) {
        TelephonyManager telephonyManager = (TelephonyManager)
                DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
        CustomPhoneStateListener phoneStateListener = new CustomPhoneStateListener(handler);
        if (telephonyManager == null) {
            phoneStateListener.sentMessage(Constant.GET_DATA_FAILED_MAYBE_NO_SIM, 0);
            return;
        }
        int simState = telephonyManager.getSimState();
        if (TelephonyManager.SIM_STATE_ABSENT == simState || TelephonyManager.SIM_STATE_UNKNOWN == simState) {
            phoneStateListener.sentMessage(Constant.GET_DATA_FAILED_MAYBE_NO_SIM, 0);
            return;
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        phoneStateListener.sentMessage(Constant.GET_DATA_FAILED_MAYBE_NO_SIM, 100);
    }

    /**
     * 获取基站id
     *
     * @param signalStrength : 手机信号强度信息，如果不需要刻意不传，可以通过getSignalStrengths方法获取
     */
    public static CustomBaseStation getBaseStation(String signalStrength) {
        final CustomBaseStation customBaseStation = new CustomBaseStation();
        try {
            customBaseStation.setBsss(signalStrength);
            final TelephonyManager telephonyManager = (TelephonyManager) DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager == null) {
                return customBaseStation;
            }
            int permission = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                List<NeighboringCellInfo> neighboringCellInfoList = telephonyManager.getNeighboringCellInfo();
                if (neighboringCellInfoList != null && neighboringCellInfoList.size() > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < neighboringCellInfoList.size(); i++) {
                            NeighboringCellInfo neighboringCellInfo = neighboringCellInfoList.get(i);
                            if (neighboringCellInfo == null) {
                                continue;
                            }
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("mCid", neighboringCellInfo.getCid());
                            jsonObject.put("mLac", neighboringCellInfo.getLac());
                            jsonObject.put("mNetworkType", neighboringCellInfo.getNetworkType());
                            jsonObject.put("mPsc", neighboringCellInfo.getPsc());
                            jsonObject.put("mRssi", neighboringCellInfo.getRssi());
                            jsonArray.put(jsonObject);
                        }
                        String neighboringCellInfoJson = jsonArray.toString();
                        customBaseStation.setStbif(neighboringCellInfoJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CellLocation cellLocation = telephonyManager.getCellLocation();
                if (cellLocation instanceof CdmaCellLocation) {
                    // 电信获取基站id
                    CdmaCellLocation location = (CdmaCellLocation) cellLocation;
                    String cid = location.getBaseStationId() + "";
                    int lac = location.getNetworkId();

                    customBaseStation.setLac(lac + "");
                    customBaseStation.setBscid(cid);
                } else if (cellLocation instanceof GsmCellLocation) {
                    // 移动联通获取基站id
                    GsmCellLocation location = (GsmCellLocation) cellLocation;
                    String cid = location.getCid() + "";
                    int lac = location.getLac();

                    customBaseStation.setLac(lac + "");
                    customBaseStation.setBscid(cid);
                } else {
                    customBaseStation.setBscid("未获取到,可能没插sim卡");
                    customBaseStation.setLac("未获取到,可能没插sim卡");
                }
            } else {
                customBaseStation.setBscid("用户拒绝权限");
                customBaseStation.setLac("用户拒绝权限");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customBaseStation;
    }
}
