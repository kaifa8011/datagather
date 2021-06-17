package com.ciba.data.gather.manager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.manager.oaid.OAIDDelegate;
import com.ciba.data.gather.util.ClassUtils;
import com.ciba.data.gather.util.LogUtils;
import com.ciba.data.synchronize.util.SPUtil;

/**
 * oaid获取
 *
 * @author parting_soul
 * @date 2019-10-24
 */
public class OAIDManager {
    private static boolean canUseOaid = true;
    private static volatile OAIDManager instance;
    private String mOAID;
    private String mVAID;

    /**
     * 是否可以获取Oaid
     *
     * @param canUseOaid
     */
    public static void setCanUseOaid(boolean canUseOaid) {
        OAIDManager.canUseOaid = canUseOaid;
    }

    public static OAIDManager getInstance() {
        if (instance == null) {
            synchronized (OAIDManager.class) {
                if (instance == null) {
                    instance = new OAIDManager();
                }
            }
        }
        return instance;
    }

    private OAIDManager() {
    }

    public void init(Context context) {
        if (!canUseOaid) {
            LogUtils.d("Oaid同步获取失败 : 不允许SDK使用oaid信息，不进行oaid初始化");
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Constant.TARGET_VERSION &&
                    ClassUtils.isLibraryCompile(Constant.OAID_LIBARY_CORE_PATH)) {
                OAIDDelegate oaidDelegate = new OAIDDelegate(context);
                oaidDelegate.setOnGetIdCallback(new OAIDDelegate.OnGetIdCallback() {
                    @Override
                    public void onIdGetSuccess(@NonNull String oaid, @NonNull String vaid) {
                        SPUtil.putString(Constant.KEY_CIBA_OAID, oaid);
                        SPUtil.putString(Constant.KEY_CIBA_VAID, vaid);
                    }
                });
                oaidDelegate.startGetOAID();
            }
        } catch (Throwable e) {
        }
    }

    public String getOAID() {
        if (mOAID == null) {
            mOAID = SPUtil.getString(Constant.KEY_CIBA_OAID);
        }
        return mOAID;
    }

    public String getVAID() {
        if (mVAID == null) {
            mVAID = SPUtil.getString(Constant.KEY_CIBA_VAID);
        }
        return mVAID;
    }

}
