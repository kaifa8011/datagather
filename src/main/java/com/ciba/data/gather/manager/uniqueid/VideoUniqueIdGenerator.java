package com.ciba.data.gather.manager.uniqueid;

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

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "video/*";
    }


}
