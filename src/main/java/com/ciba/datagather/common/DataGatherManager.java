package com.ciba.datagather.common;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import com.ciba.datagather.callback.CustomActivityLifecycleCallbacks;
import com.ciba.datagather.handler.CrashHandler;
import com.ciba.datagather.util.DataArrangeUtil;
import com.ciba.datagather.util.device.ProcessUtil;
import com.ciba.datasynchronize.common.DataSynchronizeManager;

/**
 * @author ciba
 * @description 信息收集
 * @date 2018/12/3
 */
public class DataGatherManager {
    /**
     * TODO ：更新SDK版本
     */
    private static final String SDK_VERSION = "0.3.4";
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
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Must call in the main thread!!");
        }

        this.context = context.getApplicationContext();
        this.checkRoot = checkRoot;
        DataSynchronizeManager.getInstance().init(context, DataGatherManager.getInstance().getSdkVersion());
        if (ProcessUtil.isMainProcess()) {
            initGather(context);
        }
    }

    /**
     * 初始化日志
     */
    private void initGather(Context context) {
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init();

        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(new CustomActivityLifecycleCallbacks());
        }
        DataArrangeUtil.dataGather(true, true, false, true);
    }

    /**
     * 获取上下文
     */
    public Context getContext() {
        if (context == null) {
            throw new RuntimeException("Must call init first!!");
        }
        return context;
    }

    /**
     * 是否有必要获取ROOT信息
     */
    public boolean isCheckRoot() {
        return checkRoot;
    }

    public String getSdkVersion() {
        return SDK_VERSION;
    }
}
