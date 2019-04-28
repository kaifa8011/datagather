package com.ciba.datagather.manager;

import com.ciba.datagather.util.device.PackageUtil;
import com.ciba.datasynchronize.entity.OperationData;
import com.ciba.datasynchronize.manager.DataCacheManager;
import com.ciba.datasynchronize.manager.LoaderUploaderManager;
import com.ciba.datasynchronize.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ciba
 * @description 描述
 * @date 2019/3/1
 */
public class OpertionDataManager {
    private static OpertionDataManager instance;

    private OpertionDataManager() {
    }

    public static OpertionDataManager getInstance() {
        if (instance == null) {
            synchronized (OpertionDataManager.class) {
                if (instance == null) {
                    instance = new OpertionDataManager();
                }
            }
        }
        return instance;
    }

    public void uploadOpertionData(String operationType, String scheme) {
        OperationData opertionData = createOpertionData(operationType, scheme, null, null, null, null, null, null);
        uploadOpertionData(opertionData);
    }

    public void uploadOpertionData(Map<String, String> params) {
        if (params == null) {
            return;
        }
        OperationData opertionData = createOpertionData(params.get("operationType")
                , params.get("scheme")
                , params.get("startCooX")
                , params.get("endCooX")
                , params.get("startCooY")
                , params.get("endCooY")
                , params.get("startTime")
                , params.get("endTime"));
        uploadOpertionData(opertionData);
    }

    public void uploadOpertionDatas(List<Map<String, String>> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        List<OperationData> operationDataList = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            Map<String, String> param = params.get(i);
            OperationData opertionData = createOpertionData(param.get("operationType")
                    , param.get("scheme")
                    , param.get("startCooX")
                    , param.get("endCooX")
                    , param.get("startCooY")
                    , param.get("endCooY")
                    , param.get("startTime")
                    , param.get("endTime"));
            operationDataList.add(opertionData);
        }
        uploadOpertionData(operationDataList);
    }

    private OperationData createOpertionData(String operationType
            , String scheme
            , String startCooX
            , String endCooX
            , String startCooY
            , String endCooY
            , String startTime
            , String endTime) {
        OperationData operationData = new OperationData();
        operationData.setOperationType(operationType);
        operationData.setMachineType(1);
        operationData.setScheme(scheme);
        operationData.setStartCooX(startCooX);
        operationData.setEndCooX(endCooX);
        operationData.setStartCooY(startCooY);
        operationData.setEndCooY(endCooY);
        String currentTime = TimeUtil.getCurrentTime();
        operationData.setStartTime(startTime == null ? currentTime : startTime);
        operationData.setEndTime(endTime == null ? currentTime : endTime);
        operationData.setPackageName(PackageUtil.getPackageName());
        operationData.setVersionNo(PackageUtil.getVersionName());
        operationData.setMachineId(DataCacheManager.getInstance().getMachineId());
        return operationData;
    }

    private void uploadOpertionData(OperationData operationData) {
        if (operationData == null) {
            return;
        }
        List<OperationData> operationDataList = new ArrayList<>();
        operationDataList.add(operationData);
        uploadOpertionData(operationDataList);
    }

    private void uploadOpertionData(List<OperationData> operationDataList) {
        if (operationDataList == null || operationDataList.isEmpty()) {
            return;
        }
        LoaderUploaderManager.getInstance().uploadOperationData(operationDataList);
    }

}
