package com.ciba.data.gather.constant;

import android.os.Build;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/3
 */
public class Constant {
    public static final String GET_DATA_FAILED_MAYBE_NO_SIM = "未获取到,可能没插sim卡";
    public static final String GET_DATA_FAILED_MAYBE_NO_PERMISSION = "用户拒绝权限";
    public static final String GET_DATA_NULL = "未获取到";

    public static final String NETWORK_DEFAULT_TYPE = "";
    public static final String KEY_CIBA_OAID = "KEY_CIBA_OAID";
    public static final String KEY_CIBA_VAID = "KEY_CIBA_VAID";

    /**
     * 该键不能被修改
     */
    public static final String KEY_CIBA_UNIQUE_ID = "KEY_CIBA_UNIQUE_ID";

    /**
     * Oaid库的初始化类路径
     */
    public static final String OAID_LIBARY_CORE_PATH = "com.bun.miitmdid.core.MdidSdkHelper";

    public static final int TARGET_VERSION = Build.VERSION_CODES.O;
}
