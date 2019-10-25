package com.ciba.callback;

import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.supplier.IdSupplier;

/**
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
