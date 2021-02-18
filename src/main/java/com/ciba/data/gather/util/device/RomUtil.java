package com.ciba.data.gather.util.device;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * @author songzi
 * @description 与手机硬盘相关的工具类
 * @date 2021/2/18
 */
public class RomUtil {

    /**
     * ROM大小,单位G
     */
    public static String getRomSpaceTotalSize(Context context) {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            String totalSize = Formatter.formatFileSize(context, totalBlocks * blockSize);
            return totalSize;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * ROM可用大小,单位G
     */
    public static String getRomSpaceAvailSize(Context context) {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            String availableSize = Formatter.formatFileSize(context, availableBlocks * blockSize);
            return availableSize;
        } catch (Exception e) {
        }
        return "0";
    }

}
