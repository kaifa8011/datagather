package com.ciba.data.gather.manager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ciba
 * @description 描述
 * @date 2019/8/24
 */
public class UniqueIdManager {
    private static final String UNIQUE_ID_DISPLAY_NAME = "9493fc1f6f98527c0d455b074eecedbe";
    private static UniqueIdManager instance;
    private String mUniqueId;

    private UniqueIdManager() {
    }

    public static UniqueIdManager getInstance() {
        if (instance == null) {
            synchronized (UniqueIdManager.class) {
                if (instance == null) {
                    instance = new UniqueIdManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取本地唯一标识码（可能为空，用户手动删除之后会重置）
     */
    public synchronized String getUniqueId(Context context) {
        if (context != null && TextUtils.isEmpty(mUniqueId)) {
            boolean hasPermission = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                hasPermission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasPermission) {
                mUniqueId = getUniqueIdUp29(context);
            }
        }
        return mUniqueId;
    }

    /**
     * Android 10 及以上获取本地唯一标识码
     */
    private String getUniqueIdUp29(Context context) {
        Cursor cursor = null;
        String uniqueId = null;
        FileInputStream inputStream = null;
        ParcelFileDescriptor fileDescriptor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , new String[]{MediaStore.Images.Media._ID}
                    , MediaStore.Images.Media.DISPLAY_NAME + " = ?"
                    , new String[]{UNIQUE_ID_DISPLAY_NAME}
                    , null);
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                if (!TextUtils.isEmpty(id)) {
                    Uri uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + id);
                    fileDescriptor = resolver.openFileDescriptor(uri, "rw");
                    if (fileDescriptor != null) {
                        inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
                        int len = -1;
                        byte[] bytes = new byte[64];
                        StringBuilder builder = new StringBuilder();
                        if ((len = inputStream.read(bytes)) != -1) {
                            builder.append(new String(bytes, 0, len));
                        }
                        uniqueId = builder.toString();
                        if (TextUtils.isEmpty(uniqueId)) {
                            resolver.delete(uri, null, null);
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cursor, inputStream, fileDescriptor);
        }
        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = createUniqueIdUp29(context);
        }
        return uniqueId;
    }

    /**
     * Android 10 及以上创建本地唯一标识码
     */
    private String createUniqueIdUp29(Context context) {
        ParcelFileDescriptor fileDescriptor = null;
        FileOutputStream outputStream = null;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        String uniqueId = null;
        try {
            File uniqueIdFile = null;
            ContentValues values = new ContentValues();
            if (Build.VERSION.SDK_INT < 29) {
                uniqueIdFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), UNIQUE_ID_DISPLAY_NAME);
                values.put(MediaStore.Images.Media.DATA, uniqueIdFile.getAbsolutePath());
            }
            values.put(MediaStore.Images.Media.DISPLAY_NAME, UNIQUE_ID_DISPLAY_NAME);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (uri != null) {
                if (Build.VERSION.SDK_INT >= 29) {
                    fileDescriptor = resolver.openFileDescriptor(uri, "rw");
                    if (fileDescriptor != null) {
                        outputStream = new FileOutputStream(fileDescriptor.getFileDescriptor());
                    }
                } else {
                    outputStream = new FileOutputStream(uniqueIdFile);
                }
                if (outputStream != null) {
                    uniqueId = UUID.randomUUID().toString().replace("-", "");
                    outputStream.write(uniqueId.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (TextUtils.isEmpty(uniqueId) && uri != null) {
                resolver.delete(uri, null, null);
            }
            close(fileDescriptor, outputStream);
        }
        return uniqueId;
    }

    private void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable close : closeables) {
            if (close != null) {
                try {
                    close.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
