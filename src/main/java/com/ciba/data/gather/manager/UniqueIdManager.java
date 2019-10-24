package com.ciba.data.gather.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.ciba.data.gather.manager.uniqueid.AudioUniqueIdGenerator;
import com.ciba.data.gather.manager.uniqueid.BaseUniqueIdGenerator;
import com.ciba.data.gather.manager.uniqueid.ImageUniqueGenerator;
import com.ciba.data.gather.manager.uniqueid.UniqueIdChain;
import com.ciba.data.gather.manager.uniqueid.VideoUniqueIdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciba
 * @description 描述
 * @date 2019/8/24
 */
public class UniqueIdManager {
    private volatile static UniqueIdManager instance;
    private String mUniqueId;
    private Context mContext;

    private List<BaseUniqueIdGenerator> mUniqueIdGenerators;
    private UniqueIdChain mUniqueIdChain;


    private UniqueIdManager(@NonNull Context context) {
        this.mContext = context;
        generateUniqueIdChain();
    }

    public static UniqueIdManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (UniqueIdManager.class) {
                if (instance == null) {
                    instance = new UniqueIdManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 创建uniqueId文件生成链
     */
    private void generateUniqueIdChain() {
        mUniqueIdGenerators = new ArrayList<>();
//        mUniqueIdGenerators.add(new VideoUniqueIdGenerator(mContext));
        mUniqueIdGenerators.add(new ImageUniqueGenerator(mContext));
        mUniqueIdGenerators.add(new AudioUniqueIdGenerator(mContext));
        mUniqueIdChain = new UniqueIdChain(mContext, mUniqueIdGenerators, 0);
    }

    /**
     * 获取本地唯一标识码（可能为空，用户手动删除之后会重置）
     */
    public synchronized String getUniqueId() {
        if (mContext != null && TextUtils.isEmpty(mUniqueId)) {
            boolean hasPermission = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                hasPermission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasPermission) {
                mUniqueId = getUniqueIdInner();
            }
        }
        return mUniqueId;
    }

    /**
     * 获取本地唯一标识码
     */
    private String getUniqueIdInner() {
        return mUniqueIdChain.process();
    }

}
