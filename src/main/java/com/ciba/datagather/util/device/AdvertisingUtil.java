package com.ciba.datagather.util.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;

import com.ciba.datagather.common.DataGatherManager;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : ciba
 * @date : 2018/7/23
 * @description : replace your description
 */

public class AdvertisingUtil {
    /**
     * 这个方法是耗时的，不能在主线程调用
     */
    public static String getGoogleAdId() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return "Cannot call in the main thread, You must call in the other thread";
        }
        try {
            PackageManager pm = DataGatherManager.getInstance().getContext().getPackageManager();
            pm.getPackageInfo("com.android.vending", 0);
            AdvertisingConnection connection = new AdvertisingConnection();
            Intent intent = new Intent(
                    "com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            boolean bindService = DataGatherManager.getInstance().getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            if (bindService) {
                try {
                    AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                    return adInterface.getId();
                } finally {
                    DataGatherManager.getInstance().getContext().unbindService(connection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) {
                throw new IllegalStateException();
            }
            this.retrieved = true;
            return this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }

        @Override
        public IBinder asBinder() {
            return binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }
    }
}
