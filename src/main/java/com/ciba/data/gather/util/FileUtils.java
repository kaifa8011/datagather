package com.ciba.data.gather.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
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

    public static String readFile(String fileName) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(baos);
        }
        return result;
    }

}
