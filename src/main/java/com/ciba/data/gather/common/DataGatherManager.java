package com.ciba.data.gather.common;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ciba.data.gather.callback.CustomActivityLifecycleCallbacks;
import com.ciba.data.gather.entity.DataGatherConfig;
import com.ciba.data.gather.listener.PackageInfoListener;
import com.ciba.data.gather.manager.OAIDManager;
import com.ciba.data.gather.util.DataArrangeUtil;
import com.ciba.data.gather.util.PckLogJsonUtil;
import com.ciba.data.gather.util.device.LocationUtil;
import com.ciba.data.gather.util.device.PackageUtil;
import com.ciba.data.gather.util.device.PhoneStateUtil;
import com.ciba.data.gather.util.device.ProcessUtil;
import com.ciba.data.gather.util.device.WifiUtil;
import com.ciba.data.synchronize.OnDeviceDataUpLoadListener;
import com.ciba.data.synchronize.common.DataSynchronizeManager;
import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.manager.DataCacheManager;
import com.ciba.data.synchronize.manager.LoaderUploaderManager;
import com.ciba.data.synchronize.sample.manager.SampleLoaderUploaderManager;
import com.ciba.data.synchronize.sample.manager.SampleUrlManager;
import com.ciba.data.synchronize.util.DataSynchronizeLog;
import com.ciba.data.synchronize.util.InstallListReadTimeController;
import com.ciba.datagather.BuildConfig;
import com.ciba.http.client.AsyncHttpClient;
import com.ciba.http.listener.SimpleHttpListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ciba
 * @description 信息收集
 * @date 2018/12/3
 */
public class DataGatherManager {

    private static DataGatherManager instance;
    private boolean checkRoot;
    private Context context;

    private DataGatherManager() {
    }

    public static DataGatherManager getInstance() {
        if (instance == null) {
            synchronized (DataGatherManager.class) {
                if (instance == null) {
                    instance = new DataGatherManager();
                }
            }
        }
        return instance;
    }


    /**
     * 设置数据收集的配置信息,在init方法或者initGather方法前调用
     *
     * @param config
     */
    public void setDataGatherConfig(DataGatherConfig config) {
        if (config == null) {
            return;
        }
        WifiUtil.setCanGetWifiInfo(config.isCanGetWifiInfo());
        LocationUtil.setCanUseLocation(config.isCanUseLocation());
        PhoneStateUtil.setCanGetPhoneStateInfo(config.isCanGetPhoneStateInfo());
    }

    public void init(Context context) {
        init(context, false);
    }

    /**
     * 初始化
     *
     * @param context   ：上下文
     * @param checkRoot ：是否有必要检查ROOT状态
     */
    public void init(Context context, boolean checkRoot) {
        boot(context, checkRoot);
        initGather();
    }

    /**
     * 初始化
     *
     * @param context   ：上下文
     * @param checkRoot ：是否有必要检查ROOT状态
     */
    public void boot(Context context, boolean checkRoot) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Must call in the main thread!!");
        }

        this.context = context.getApplicationContext();
        this.checkRoot = checkRoot;
        DataSynchronizeManager.getInstance().init(context, DataGatherManager.getInstance().getSdkVersion());
    }

    /**
     * 初始化日志
     */
    public void initGather() {
        if (context != null && Looper.getMainLooper() == Looper.myLooper() && ProcessUtil.isMainProcess()) {
            OAIDManager.getInstance().init(context);
            if (context instanceof Application) {
                ((Application) context).registerActivityLifecycleCallbacks(new CustomActivityLifecycleCallbacks());
            }
            DataArrangeUtil.dataGather(true, true, false, true, null, false);
        }
    }


    /**
     * 获取machineId给外部调用
     *
     * @param listener
     */
    public void getMachineId(OnDeviceDataUpLoadListener listener) {
        //初始化判断
        getContext();
        long machineId = DataCacheManager.getInstance().getMachineId();
        if (machineId == 0) {
            DataArrangeUtil.dataGather(true, true, false, false, listener, true);
        } else if (listener != null) {
            listener.onUploadSuccess(machineId);
        }
    }

    /**
     * 获取上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * 是否有必要获取ROOT信息
     */
    public boolean isCheckRoot() {
        return checkRoot;
    }

    /**
     * 获取并上传手机安装列表信息
     */
    public void upLoadInstallPackage() {
        try {
            final long machineId = DataCacheManager.getInstance().getMachineId();
            if (machineId == 0) {
                return;
            }

            boolean localInstallListRead = InstallListReadTimeController.isLocalInstallListRead();
            if (!localInstallListRead) {
                return;
            }
            InstallListReadTimeController.saveLocalInstallListReadTime();

            //异步获取安装列表并上报
            Handler handler = new Handler();
            PackageUtil.asynInstallPackageList(handler, true, new PackageInfoListener() {
                @Override
                public void onPackgeInfoFinished(List<CustomPackageInfo> installPackageList) {
                    if (installPackageList != null && installPackageList.size() > 0) {
                        LoaderUploaderManager.getInstance().uploadInstallData(installPackageList);
                    } else {
                        // 如果常规方案没有读取到安装列表
                        requestInstallPackageList(machineId);
                    }
                }
            });
        } catch (Exception e) {}
    }

    /**
     * 请求配置扫描目录判断包名
     */
    public void requestInstallPackageList(final long machineId) {
        //判断版本号在29以下
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            return;
        }

        Map<String, String> requestParams = new HashMap<>(1);
        requestParams.put("machineId", machineId + "");
        String installPackageUrl = SampleUrlManager.getInstallPackageUrl();
        AsyncHttpClient httpClient = SampleLoaderUploaderManager.getInstance().getHttpClient();
        httpClient.post(installPackageUrl, requestParams, new SimpleHttpListener() {
                    @Override
                    public void onRequestSuccess(String result) {
                        DataSynchronizeLog.innerI("0x00000010");
                        try {
                            String resultJson = PckLogJsonUtil.parseDataJson(result);
                            if (!TextUtils.isEmpty(resultJson)) {
                                PckLogJsonUtil.packagePathData(machineId, resultJson);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }


    public String getSdkVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
