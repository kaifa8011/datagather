package com.ciba.data.gather.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ciba.data.gather.constant.Constant;
import com.ciba.data.gather.manager.oaid.OAIDDelegate;
import com.ciba.data.gather.util.ClassUtils;
import com.ciba.data.synchronize.util.SPUtil;

/**
 * oaid获取
 *
 * @author parting_soul
 * @date 2019-10-24
 */
public class OAIDManager {
    private static OAIDManager instance;
    private String mOAID;

    public static OAIDManager getInstance() {
        if (instance == null) {
            synchronized (UniqueIdManager.class) {
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
        if (ClassUtils.isLibraryCompile(Constant.OAID_LIBARY_CORE_PATH)) {
            OAIDDelegate oaidDelegate = new OAIDDelegate(context);
            oaidDelegate.startGetOAID();
            oaidDelegate.setOnGetIdCallback(new OAIDDelegate.OnGetIdCallback() {
                @Override
                public void onIdGetSuccess(@NonNull String ids) {
                    SPUtil.putString(Constant.KEY_ADMOBILE_OAID, ids);
                }
            });
        }
    }

    public String getOAID() {
        if (mOAID == null) {
            mOAID = SPUtil.getString(Constant.KEY_ADMOBILE_OAID);
        }
        return mOAID;
    }

}