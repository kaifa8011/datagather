package com.ciba.data.gather.entity;

/**
 * @author songzi
 * @date 2021/5/21
 */
public class PakFileInfo {
    private String packageName;
    private String fileName;
    private String filePath;

    public PakFileInfo(String packageName, String fileName, String filePath) {
        this.packageName = packageName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFileName() {
        return fileName;
    }
}
