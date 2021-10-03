package com.ciba.data.gather.manager.oaid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.InfoCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.bun.miitmdid.pojo.IdSupplierImpl;
import com.ciba.callback.IIdentifierListenerImp;
import com.ciba.data.gather.util.ExceptionUtils;
import com.ciba.data.synchronize.util.StateUtil;

/**
 * oaid获取
 *
 * @author parting_soul
 * @date 2019-10-24
 */
public class OAIDDelegate {

    private OnGetIdCallback mListener;
    private Context mContext;

    public OAIDDelegate(@NonNull Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setOnGetIdCallback(OnGetIdCallback callback) {
        this.mListener = callback;
    }


    /**
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     */
    private int callFromReflect(Context cxt) {
        IIdentifierListenerImp listener = new IIdentifierListenerImp();
        listener.setCallback(new IIdentifierListenerImp.Callback() {
            @Override
            public void OnSupport(IdSupplier idSupplier) {
                if (idSupplier == null) {
                    return;
                }
                // 防止异步调用出现异常
                try {
                    String oaid = idSupplier.getOAID();
                    String vaid = idSupplier.getVAID();
                    if (mListener != null) {
                        mListener.onIdGetSuccess(oaid, vaid);
                    }
                } catch (Throwable e) {
                }
            }
        });
        int result = 0;
        try {
            result = MdidSdkHelper.InitSdk(cxt, true, listener);
        } catch (Throwable e) {
            ExceptionUtils.printStackTrace(e);
        }
        return result;
    }


    public void startGetOAID() {
        int code = callFromReflect(mContext);
        String error = "";
        // 根据SDK返回的code进行不同处理
        if(code == InfoCode.INIT_ERROR_CERT_ERROR){
            error = "证书未初始化或证书无效";
        }else if(code == InfoCode.INIT_ERROR_DEVICE_NOSUPPORT){
            error = "证书未初始化或证书无效";
        }else if( code == InfoCode.INIT_ERROR_LOAD_CONFIGFILE){
            error = "加载配置文件出错";
        }else if(code == InfoCode.INIT_ERROR_MANUFACTURER_NOSUPPORT){
            error = "不支持的设备厂商";
        }else if(code == InfoCode.INIT_ERROR_SDK_CALL_ERROR){
            error = "sdk调用出错";
        } else if(code == InfoCode.INIT_INFO_RESULT_DELAY) {
            error = "获取接口是异步的";
        }else if(code == InfoCode.INIT_INFO_RESULT_OK){
            error = "获取接口是同步的";
        }else {
            // sdk版本高于DemoHelper代码版本可能出现的情况，无法确定是否调用onSupport
            // 不影响成功的OAID获取
            error = "getDeviceIds: unknown code: " + code;
        }
        if (!StateUtil.checkFlag() && !TextUtils.isEmpty(error)) {
            Log.e("OAIDDelegate", "startGetOAID: oaid获取失败 error " + error);
        }
    }

    public interface OnGetIdCallback {
        void onIdGetSuccess(@NonNull String oaid, @NonNull String vaid);
    }

}
