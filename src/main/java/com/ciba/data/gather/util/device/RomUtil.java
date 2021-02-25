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
     * ROM大小,单位byte
     */
    public static long getRomSpaceTotalSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } catch (Exception e) {
        }
        return 0;
    }


    /**
     * ROM可用大小,单位byte
     */
    public static long getRomSpaceAvailSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } catch (Exception e) {
        }
        return 0;
    }

}
