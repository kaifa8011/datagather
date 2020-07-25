package com.ciba.data.gather.util.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ciba.data.gather.common.DataGatherManager;


/**
 * @author parting_soul
 * @date 2020-07-16
 */
public class AirPressureUtils {

    public static void getAirPressure(final OnGetAirPressCallback callback) {
        /*获取系统服务（SENSOR_SERVICE）返回一个SensorManager对象*/
        final SensorManager sensorManager = (SensorManager) DataGatherManager.getInstance().getContext().
                getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null || sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null) {
            if (callback != null) {
                callback.onGetAirPress(0);
            }
            return;
        }
        /*通过SensorManager获取相应的（压力传感器）Sensor类型对象*/
        Sensor mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                    /*压力传感器返回当前的压强，单位是百帕斯卡hectopascal（hPa）。*/
                    float pressure = event.values[0];
                    if (callback != null) {
                        callback.onGetAirPress(pressure);
                    }
                }
                sensorManager.unregisterListener(this);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public interface OnGetAirPressCallback {
        void onGetAirPress(float pressure);
    }

}
