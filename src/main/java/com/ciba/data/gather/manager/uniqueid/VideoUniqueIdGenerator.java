package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 存放在视频媒体目录uniqueId文件
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class VideoUniqueIdGenerator extends BaseUniqueIdGenerator {

    public VideoUniqueIdGenerator(Context context) {
        super(context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String getMediaFileSuffix() {
        return ".mp4";
    }

    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "video/mp4";
    }

    @Override
    protected String getBucketDisplayName() {
        return Environment.DIRECTORY_MOVIES;
    }

    @Override
    protected String getMediaRawData() {
        return "";
    }

    @Override
    protected String getMediaFilePrefix() {
        return "421b47ffd946ca083b65cd668c6b17e6";
    }

}
