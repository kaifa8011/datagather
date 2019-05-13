package com.ciba.datagather.entity;

import com.ciba.datagather.util.device.PackageUtil;
import com.ciba.datasynchronize.entity.OperationData;
import com.ciba.datasynchronize.manager.DataCacheManager;
import com.ciba.datasynchronize.util.TimeUtil;

import java.util.Map;

/**
 * @author ciba
 * @description 描述
 * @date 2019/5/13
 */
public class CustomOperationData extends OperationData {
    public CustomOperationData(String operationType, String scheme) {
        setOperationType(operationType);
        setMachineType(1);
        setScheme(scheme);
        setStartTime(TimeUtil.getCurrentTime());
        setPackageName(PackageUtil.getPackageName());
        setVersionNo(PackageUtil.getVersionName());
        setMachineId(DataCacheManager.getInstance().getMachineId());
    }

    @Override
    public void setMachineType(int machineType) {
        super.setMachineType(machineType);
    }

    @Override
    public void setOperationType(String operationType) {
        super.setOperationType(operationType);
    }

    @Override
    public void setScheme(String scheme) {
        super.setScheme(scheme);
    }

    @Override
    public void setStartCooX(String startCooX) {
        super.setStartCooX(startCooX);
    }

    @Override
    public void setEndCooX(String endCooX) {
        super.setEndCooX(endCooX);
    }

    @Override
    public void setStartCooY(String startCooY) {
        super.setStartCooY(startCooY);
    }

    @Override
    public void setEndCooY(String endCooY) {
        super.setEndCooY(endCooY);
    }

    @Override
    public void setStartTime(String startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public void setEndTime(String endTime) {
        super.setEndTime(endTime);
    }

    @Override
    public void setPackageName(String packageName) {
        super.setPackageName(packageName);
    }

    @Override
    public void setVersionNo(String versionNo) {
        super.setVersionNo(versionNo);
    }

    @Override
    public void setMachineId(long machineId) {
        super.setMachineId(machineId);
    }

    @Override
    public void setCustomParam(Map<String, String> customParam) {
        super.setCustomParam(customParam);
    }
}
