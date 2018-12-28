package com.ciba.datagather.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ciba.datagather.constant.Constant;
import com.ciba.datagather.common.DataGatherManager;


/**
 * @author ciba
 * @description 自定义的信号强度变化监听
 * @date 2018/12/4
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    private final Handler handler;
    private boolean sent;
    private long preMillis;
    private long time;

    public CustomPhoneStateListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        TelephonyManager telephonyManager = (TelephonyManager)
                DataGatherManager.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        }
        sentMessage(getBsss(signalStrength));
    }

    /**
     * 发送信号强度信息
     *
     * @param signalStrength ：信号强度信息
     */
    public void sentMessage(String signalStrength) {
        if (!sent) {
            sent = true;
            if (preMillis != 0 && System.currentTimeMillis() - preMillis > this.time) {
                return;
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                Message message = new Message();
                message.obj = signalStrength;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 发送信号强度信息
     *
     * @param time ：延迟时间（主要用于长时间没有监听到变化）
     */
    public void sentMessageDelayed(long time) {
        this.time = time;
        this.preMillis = System.currentTimeMillis();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            Message message = new Message();
            message.obj = Constant.GET_DATA_FAILED_MAYBE_NO_SIM;
            handler.sendMessageDelayed(message, time);
        }
    }

    private String getBsss(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return Constant.GET_DATA_FAILED_MAYBE_NO_SIM;
        }
        String signalStrengthStr = signalStrength.toString();
        if (TextUtils.isEmpty(signalStrengthStr)) {
            return Constant.GET_DATA_FAILED_MAYBE_NO_SIM;
        }
        return signalStrengthStr;
    }
}
