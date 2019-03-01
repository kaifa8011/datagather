package com.ciba.datagather.manager;

import android.view.MotionEvent;

import com.ciba.datagather.util.device.PackageUtil;
import com.ciba.datasynchronize.entity.OperationData;
import com.ciba.datasynchronize.manager.DataCacheManager;
import com.ciba.datasynchronize.manager.LoaderUploaderManager;
import com.ciba.datasynchronize.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciba
 * @description 描述
 * @date 2019/3/1
 */
public class MotionEventManager {
    private final OperationData operationData = new OperationData();
    private final List<OperationData> motionEventDataList = new ArrayList<>();
    private static MotionEventManager instance;
    private float startX;
    private float startY;

    private MotionEventManager() {
        operationData.setMachineId(DataCacheManager.getInstance().getMachineId());
        operationData.setPackageName(PackageUtil.getPackageName());
        operationData.setVersionNo(PackageUtil.getVersionName());
        operationData.setMachineType(1);
    }

    public static MotionEventManager getInstance() {
        if (instance == null) {
            synchronized (MotionEventManager.class) {
                if (instance == null) {
                    instance = new MotionEventManager();
                }
            }
        }
        return instance;
    }

    /**
     * 操作时间记录
     *
     * @param activityName ：当前Activity名称
     * @param event        ：事件对象
     */
    public void motionEventRecord(String activityName, MotionEvent event) {
        if (event == null || activityName == null || operationData.getMachineId() == 0) {
            return;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                operationData.setStartTime(TimeUtil.getCurrentTime());
                break;
            case MotionEvent.ACTION_UP:
                OperationData data = new OperationData();
                data.setStartTime(operationData.getStartTime());
                data.setMachineId(operationData.getMachineId());
                data.setPackageName(operationData.getPackageName());
                data.setVersionNo(operationData.getVersionNo());
                data.setMachineType(1);
                data.setScheme(activityName);
                data.setStartCooX(startX + "");
                data.setStartCooY(startY + "");
                data.setEndCooX(event.getX() + "");
                data.setEndCooY(event.getY() + "");
                data.setEndTime(TimeUtil.getCurrentTime());
                data.setOperationType(isSlide(event.getX(), event.getY()) ? "SLIDE" : OperationData.TYPE_CLICK);
                addDataAndCheckUpload(data);
                break;
            default:
                break;
        }
    }

    public void onDestroy() {
        checkUpload(0);
    }

    /**
     * 添加数据并检查上报
     *
     * @param data
     */
    private void addDataAndCheckUpload(OperationData data) {
        motionEventDataList.add(data);
        checkUpload(20);
    }

    /**
     * 检查是否需要上报
     */
    private void checkUpload(int checkNum) {
        if (motionEventDataList.size() > checkNum) {
            uploadOptionData();
        }
    }

    /**
     * 上传操作数据
     */
    private void uploadOptionData() {
        LoaderUploaderManager.getInstance().uploadMotionEventData(motionEventDataList);
        motionEventDataList.clear();
    }

    /**
     * 是否是滑动，X或Y轴坐标差值超过一定像素算作滑动
     */
    private boolean isSlide(float endX, float endY) {
        return Math.abs(endX - startX) > 20 || Math.abs(endY - startY) > 20;
    }
}
