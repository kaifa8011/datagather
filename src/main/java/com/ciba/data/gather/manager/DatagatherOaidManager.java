package com.ciba.data.gather.manager;

import android.content.Context;

import com.ciba.data.gather.util.ClassUtils;

import cn.admobiletop.adsuyi.adapter.oaid.OAIDManager;

/**
 * @Description:
 * @Author: maipian
 * @CreateDate: 2021/8/10 3:06 PM
 */
public class DatagatherOaidManager {
    private static volatile DatagatherOaidManager instance;
    private boolean isImportOAIDAdapter;
    private static String OAID_ADAPTER_MANAGER_PATH = "cn.admobiletop.adsuyi.adapter.oaid.OAIDManager";


    public static DatagatherOaidManager getInstance() {
        if (instance == null) {
            synchronized (DatagatherOaidManager.class) {
                if (instance == null) {
                    instance = new DatagatherOaidManager();
                }
            }
        }
        return instance;
    }

    public DatagatherOaidManager() {
        if (ClassUtils.isLibraryCompile(OAID_ADAPTER_MANAGER_PATH)) {
            isImportOAIDAdapter = true;
        }
    }

    /**
     * 初始化oaid适配器
     *
     * @param context
     * @param isCanUseOaid 是否支持使用OAID
     */
    public void init(Context context, boolean isCanUseOaid) {
        if (!isCanUseOaid) {
            return;
        }
        if (isImportOAIDAdapter) {
            OAIDManager.getInstance().init(context, isCanUseOaid);
        }
    }

    public String getOAID() {
        if (!isImportOAIDAdapter) {
            return "";
        }
        return OAIDManager.getInstance().getOAID();
    }

    public String getAAID() {
        if (!isImportOAIDAdapter) {
            return "";
        }
        return OAIDManager.getInstance().getAAID();
    }

    public String getVAID() {
        if (!isImportOAIDAdapter) {
            return "";
        }
        return OAIDManager.getInstance().getVAID();
    }
}
