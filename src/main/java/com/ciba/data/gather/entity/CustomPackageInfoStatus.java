package com.ciba.data.gather.entity;

/**
 * 安装列表上传状态记录类，单例
 * @author songzi
 * @date 2021/5/17
 */
public class CustomPackageInfoStatus {
    /**
     * 系统采集方法返回内容是否为空
     */
    private boolean isEmpty = true;

    /**
     * 系统采集方法执行是否结束
     */
    private boolean isFinish = false;

    /**
     * 是否调用了上次接口，若没调用则为服务端关闭上传功能，停止后续一切上传动作
     */
    private boolean isUpload = false;


    private CustomPackageInfoStatus(){}

    private static class SingletonInstance{
        private static final CustomPackageInfoStatus instance = new CustomPackageInfoStatus();
    }

    public static CustomPackageInfoStatus getInstance(){
        return SingletonInstance.instance;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

}
