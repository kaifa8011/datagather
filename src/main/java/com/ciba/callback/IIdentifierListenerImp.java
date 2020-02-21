package com.ciba.callback;


import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;

/**
 * 防止回调接口被混淆
 *
 * @author parting_soul
 * @date 2019-10-24
 */
public class IIdentifierListenerImp implements IIdentifierListener {
    private Callback mCallback;

    @Override
    public void OnSupport(boolean b, IdSupplier idSupplier) {
        if (mCallback != null) {
            mCallback.OnSupport(b, idSupplier);
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void OnSupport(boolean b, IdSupplier idSupplier);
    }

}
