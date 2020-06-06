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
import android.util.Base64;

import com.ciba.data.gather.util.ExceptionUtils;
import com.ciba.data.gather.util.FileUtils;
import com.ciba.data.synchronize.util.SPUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;
import java.util.UUID;

import static com.ciba.data.gather.util.FileUtils.close;

/**
 * uniqueId构造器
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public abstract class BaseUniqueIdGenerator {
    private String UNIQUE_ID_DISPLAY_NAME;
    private Context mContext;

    protected BaseUniqueIdGenerator(Context context) {
        mContext = context;
        UNIQUE_ID_DISPLAY_NAME = getMediaFilePrefix();
    }

    /**
     * 从已存在文件中(数据库中不存在该文件记录)读取id
     *
     * @return
     */
    protected String readUniqueIdFromDirtyFile() {
        return null;
    }

    /**
     * 获取本地唯一标识码
     *
     * @return
     */
    final String getUniqueId(UniqueIdChain chain) {
        String uniqueId = getUniqueIdInner();
        if (TextUtils.isEmpty(uniqueId) && TextUtils.isEmpty(uniqueId = readUniqueIdFromDirtyFile())) {
            if (!chain.isLastNode()) {
                uniqueId = chain.process();
                //创建存储unique的文件
                if (!TextUtils.isEmpty(uniqueId)) {
                    createUniqueId(uniqueId);
                }
            } else {
                //当前为最后一个节点
                uniqueId = createUniqueId(null);
            }
        }
        return uniqueId;
    }

    /**
     * 通过file uri去获取uniqueId
     *
     * @return
     */
    private String getUniqueIdByFileUri() {
        String volumeName = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ? "external" : "external_primary";
        String uniqueId = null;
        String bucketDisplayName = getBucketDisplayName();
        Uri rootUri = MediaStore.Files.getContentUri(volumeName);
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(rootUri
                , new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns._ID}
                , MediaStore.MediaColumns.DISPLAY_NAME + " like ? and bucket_display_name = ?"
                , new String[]{UNIQUE_ID_DISPLAY_NAME + "%" + getMediaFileSuffix(), bucketDisplayName}
                , null);
        while (cursor != null && cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
            Uri uri = MediaStore.Files.getContentUri(volumeName, id);
            if (!TextUtils.isEmpty(fileName)) {
                uniqueId = getUniqueIdFromName(fileName);
                if (!TextUtils.isEmpty(uniqueId)) {
                    break;
                }
            }
            if (uri != null && TextUtils.isEmpty(uniqueId)) {
                resolver.delete(uri, null, null);
            }
        }
        FileUtils.close(cursor);
        return uniqueId;
    }


    /**
     * 通过具体的媒体uri去获取uniqueId(Image,Video,Audio)
     * 可能数据库存在uri但是通过该方式无法读取
     *
     * @param mediaUri
     * @return
     */
    private String getUniqueIdByConcreteMediaUri(Uri mediaUri) {
        String uniqueId = null;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(mediaUri
                , new String[]{MediaStore.MediaColumns.DISPLAY_NAME}
                , MediaStore.MediaColumns.DISPLAY_NAME + " like ?"
                , new String[]{UNIQUE_ID_DISPLAY_NAME + "%" + getMediaFileSuffix()}
                , null);
        while (cursor != null && cursor.moveToNext()) {
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
            if (!TextUtils.isEmpty(fileName)) {
                uniqueId = getUniqueIdFromName(fileName);
                if (!TextUtils.isEmpty(uniqueId)) {
                    break;
                }
            }
        }
        FileUtils.close(cursor);
        return uniqueId;
    }

    /**
     * 从文件名中获取uniqueId
     *
     * @param name
     * @return
     */
    private String getUniqueIdFromName(String name) {
        String uniqueId = null;
        try {
            if (!TextUtils.isEmpty(name)) {
                uniqueId = name.substring(UNIQUE_ID_DISPLAY_NAME.length(), name.length() - getMediaFileSuffix().length());
            }
        } catch (Exception e) {
            uniqueId = null;
        }
        return uniqueId;
    }

    /**
     * 获取当前媒体文件下的UniqueId
     *
     * @return
     */
    final String getUniqueIdInner() {
        String uniqueId;
        Uri mediaUri = getMediaUri();
        uniqueId = getUniqueIdByConcreteMediaUri(mediaUri);
        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = getUniqueIdByFileUri();
        }
        return uniqueId;
    }

    /**
     * 创建本地唯一标识码
     *
     * @param existUniqueId
     * @return
     */
    final String createUniqueId(String existUniqueId) {
        if (!isMatchGenerateRule()) {
            return null;
        }
        ParcelFileDescriptor fileDescriptor = null;
        FileOutputStream outputStream = null;
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = null;
        String uniqueId = null;
        try {
            File uniqueIdFile = null;
            ContentValues values = new ContentValues();
            boolean isExistUniqueId = !TextUtils.isEmpty(existUniqueId);
            uniqueId = isExistUniqueId ? existUniqueId : UUID.randomUUID().toString().replace("-", "");

            String uniqueIdFileName = UNIQUE_ID_DISPLAY_NAME + uniqueId + getMediaFileSuffix();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                File parentDir = getUniqueFileDir();
                if (!parentDir.exists()) {
                    //不存在父文件目录则创建
                    parentDir.mkdir();
                }
                uniqueIdFile = new File(getUniqueFileDir(), uniqueIdFileName);
                values.put(MediaStore.MediaColumns.DATA, uniqueIdFile.getAbsolutePath());
            }

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, uniqueIdFileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, getUniqueFileMimeType());
            uri = resolver.insert(getMediaUri(), values);
            if (uri != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    fileDescriptor = resolver.openFileDescriptor(uri, "rw");
                    if (fileDescriptor != null) {
                        outputStream = new FileOutputStream(fileDescriptor.getFileDescriptor());
                    }
                } else {
                    outputStream = new FileOutputStream(uniqueIdFile);
                }
                if (outputStream != null) {
                    outputStream.write(getMediaData());
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
     * 获取媒体数据
     *
     * @return
     */
    private byte[] getMediaData() {
        try {
            return Base64.decode(getMediaRawData(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "".getBytes();
    }

    /**
     * 获取媒体元数据
     *
     * @return
     */
    protected abstract String getMediaRawData();


    /**
     * 媒体类型目录uri
     *
     * @return
     */
    protected abstract Uri getMediaUri();

    /**
     * 媒体文件后缀
     *
     * @return
     */
    protected abstract String getMediaFileSuffix();

    /**
     * 媒体文件前缀
     *
     * @return
     */
    protected abstract String getMediaFilePrefix();

    /**
     * @return uniqueId文件的存放目录
     */
    protected abstract File getUniqueFileDir();

    /**
     * @return 插入媒体文件的MIME类型
     */
    protected abstract String getUniqueFileMimeType();

    protected abstract String getBucketDisplayName();


    /**
     * 获取uniqueId文件生成规则的key
     *
     * @return
     */
    protected abstract String getUniqueIdGenerateRuleKey();


    /**
     * 符合生成规则
     *
     * @return
     */
    private boolean isMatchGenerateRule() {
        boolean isCreate;
        try {
            Set<String> rules = SPUtil.getStringSet("marks");
            isCreate = rules.contains(getUniqueIdGenerateRuleKey());
        } catch (Exception e) {
            isCreate = isGenerateWhenException();
        }
        return isCreate;
    }

    /**
     * 指定异常情况下是否生成(配置信息获取失败)
     *
     * @return
     */
    protected abstract boolean isGenerateWhenException();

}
