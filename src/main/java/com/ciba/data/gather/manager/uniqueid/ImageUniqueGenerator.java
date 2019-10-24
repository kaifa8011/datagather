package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 存放在图片媒体目录uniqueId文件 必须放在责任链最后
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class ImageUniqueGenerator extends BaseUniqueIdGenerator {

    public ImageUniqueGenerator(Context context) {
        super(context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    @Override
    protected String getMediaFileSuffix() {
        return ".png";
    }

    @Override
    protected String getMediaFilePrefix() {
        return "5eb63bbbe01eeed093cb22bb8f5acdc3";
    }

    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "image/png";
    }

    @Override
    protected String getBucketDisplayName() {
        return Environment.DIRECTORY_PICTURES;
    }


    @Override
    protected String getMediaRawData() {
        return "iVBORw0KGgoAAAANSUhEUgAAAAIAAAADAQMAAACDJEzCAAAAA1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjAAMAAAYAAegKKqQAAAAASUVORK5CYII=";
    }

}
