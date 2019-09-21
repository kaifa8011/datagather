package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * 存放在图片媒体目录uniqueId文件 必须放在责任链最后
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class ImageUniqueGenerator extends BaseUniqueIdGenerator {
    
    @Override
    protected Uri getMediaUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "image/*";
    }

    @Override
    String getUniqueId(UniqueIdChain chain) {
        Context context = chain.getContext();
        String uniqueId = getUniqueIdInner(context);
        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = createUniqueId(context, null);
        }
        return uniqueId;
    }

}
