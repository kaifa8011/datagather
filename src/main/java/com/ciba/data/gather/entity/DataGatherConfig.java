package com.ciba.data.gather.entity;

/**
 * 数据收集配置
 *
 * @author parting_soul
 * @date 2020-02-13
 */
public class DataGatherConfig {
    private boolean canUseLocation;
    private boolean canGetPhoneStateInfo;
    private boolean canGetWifiInfo;

    private DataGatherConfig(Builder builder) {
        this.canGetPhoneStateInfo = builder.canGetPhoneStateInfo;
        this.canGetWifiInfo = builder.canGetWifiInfo;
        this.canUseLocation = builder.canUseLocation;
    }

    public static class Builder {
        private boolean canUseLocation = true;
        private boolean canGetPhoneStateInfo = true;
        private boolean canGetWifiInfo = true;

        /**
         * 权限允许下，是否可以获取位置信息
         *
         * @param canUseLocation
         * @return
         */
        public Builder setCanUseLocation(boolean canUseLocation) {
            this.canUseLocation = canUseLocation;
            return this;
        }

        /**
         * 在权限允许下是否可以获取手机状态信息
         *
         * @param canGetPhoneStateInfo
         * @return
         */
        public Builder setCanGetPhoneStateInfo(boolean canGetPhoneStateInfo) {
            this.canGetPhoneStateInfo = canGetPhoneStateInfo;
            return this;
        }

        /**
         * 权限允许下是否可以获取wifi信息
         *
         * @param canGetWifiInfo
         * @return
         */
        public Builder setCanGetWifiInfo(boolean canGetWifiInfo) {
            this.canGetWifiInfo = canGetWifiInfo;
            return this;
        }

        public DataGatherConfig build() {
            return new DataGatherConfig(this);
        }

    }


    public boolean isCanUseLocation() {
        return canUseLocation;
    }

    public boolean isCanGetPhoneStateInfo() {
        return canGetPhoneStateInfo;
    }

    public boolean isCanGetWifiInfo() {
        return canGetWifiInfo;
    }
}
