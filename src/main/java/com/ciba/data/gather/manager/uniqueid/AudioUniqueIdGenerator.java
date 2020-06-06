package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 存放在音频媒体目录uniqueId文件
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class AudioUniqueIdGenerator extends BaseUniqueIdGenerator {

    public AudioUniqueIdGenerator(Context context) {
        super(context);
    }

    @Override
    protected String getMediaRawData() {
        return "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";
    }

    @Override
    protected String getMediaFileSuffix() {
        return ".mp3";
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "audio/mp3";
    }

    @Override
    protected String getBucketDisplayName() {
        return Environment.DIRECTORY_MUSIC;
    }

    @Override
    protected String getUniqueIdGenerateRuleKey() {
        return "audio";
    }

    @Override
    protected boolean isGenerateWhenException() {
        return true;
    }

    @Override
    protected String getMediaFilePrefix() {
        return "50c01294df9d48902639978235bcb56c";
    }

}
