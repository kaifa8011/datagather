package com.ciba.data.gather.manager.uniqueid;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ciba.data.gather.util.ExceptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

import static com.ciba.data.gather.util.FileUtils.close;

/**
 * uniqueId构造器
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public abstract class BaseUniqueIdGenerator {
    private static final String UNIQUE_ID_DISPLAY_NAME = "9493fc1f6f98527c0d455b074eecedbe";


    /**
     * 从已存在文件中(数据库中不存在该文件记录)读取id
     *
     * @param context
     * @return
     */
    protected String readUniqueIdFromDirtyFile(Context context) {
//        String uniqueId = null;
//        //已存在文件，读取文件内容
//        File uniqueIdFile = new File(getUniqueFileDir(), UNIQUE_ID_DISPLAY_NAME);
//        if (uniqueIdFile.exists()) {
//            //文件夹存在,读出文件中内容，删除文件
//            uniqueId = FileUtils.readFile(uniqueIdFile.getAbsolutePath());
//            boolean delete = uniqueIdFile.delete();
//            LogUtils.e("delete = " + delete);
//            if (!TextUtils.isEmpty(uniqueId)) {
//                uniqueId = createUniqueId(context, uniqueId);
//            }
//        }
//        return uniqueId;
        return null;
    }

    /**
     * 获取本地唯一标识码
     *
     * @return
     */
    String getUniqueId(UniqueIdChain chain) {
        Context context = chain.getContext();
        String uniqueId = getUniqueIdInner(context);
        if (TextUtils.isEmpty(uniqueId) && TextUtils.isEmpty(uniqueId = readUniqueIdFromDirtyFile(context))) {
            uniqueId = chain.process();
            //创建存储unique的文件
            if (!TextUtils.isEmpty(uniqueId)) {
                uniqueId = createUniqueId(context, uniqueId);
            }
        }
        return uniqueId;
    }

    /**
     * 获取当前媒体文件下的UniqueId
     *
     * @param context
     * @return
     */
    String getUniqueIdInner(Context context) {
        Cursor cursor = null;
        String uniqueId = null;
        FileInputStream inputStream = null;
        ParcelFileDescriptor fileDescriptor = null;

        try {
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(getMediaUri()
                    , new String[]{MediaStore.MediaColumns._ID}
                    , MediaStore.MediaColumns.DISPLAY_NAME + " = ?"
                    , new String[]{UNIQUE_ID_DISPLAY_NAME}
                    , null);
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                if (!TextUtils.isEmpty(id)) {
                    Uri uri = Uri.parse(getMediaUri() + "/" + id);
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
            ExceptionUtils.printStackTrace(e);
        } finally {
            close(cursor, inputStream, fileDescriptor);
        }
        return uniqueId;
    }


    /**
     * 创建本地唯一标识码
     *
     * @param existUniqueId
     * @return
     */
    String createUniqueId(Context context, String existUniqueId) {
        ParcelFileDescriptor fileDescriptor = null;
        FileOutputStream outputStream = null;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        String uniqueId = null;
        try {
            File uniqueIdFile = null;
            ContentValues values = new ContentValues();
            if (Build.VERSION.SDK_INT < 29) {

                File parentDir = getUniqueFileDir();
                if (!parentDir.exists()) {
                    //不存在父文件目录则创建
                    parentDir.mkdir();
                }
                uniqueIdFile = new File(getUniqueFileDir(), UNIQUE_ID_DISPLAY_NAME);
                values.put(MediaStore.MediaColumns.DATA, uniqueIdFile.getAbsolutePath());
            }
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, UNIQUE_ID_DISPLAY_NAME);
            values.put(MediaStore.MediaColumns.MIME_TYPE, getUniqueFileMimeType());
            uri = resolver.insert(getMediaUri(), values);
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

                    boolean isExistUniqueId = !TextUtils.isEmpty(existUniqueId);
                    uniqueId = isExistUniqueId ? existUniqueId : UUID.randomUUID().toString().replace("-", "");

                    outputStream.write(uniqueId.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            if (TextUtils.isEmpty(uniqueId) && uri != null) {
                resolver.delete(uri, null, null);
            }
            close(fileDescriptor, outputStream);
            if (uniqueId == null) {
                uniqueId = existUniqueId;
            }
        }
        return uniqueId;
    }


    /**
     * 媒体类型目录uri
     *
     * @return
     */
    protected abstract Uri getMediaUri();

    /**
     * @return uniqueId文件的存放目录
     */
    protected abstract File getUniqueFileDir();

    /**
     * @return 插入媒体文件的MIME类型
     */
    protected abstract String getUniqueFileMimeType();


}
