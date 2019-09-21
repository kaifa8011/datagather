package com.ciba.data.gather.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author parting_soul
 * @date 2019-09-19
 */
public class FileUtils {

    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable close : closeables) {
            if (close != null) {
                try {
                    close.close();
                } catch (IOException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
    }

}
