package com.ciba.data.gather.util.device;

import java.lang.reflect.Method;

/**
 * @Description: 反射类实现SystemProperties调用
 * @Author: maipian
 * @CreateDate: 2021/1/5 9:28 AM
 */
public class PropertyUtils {
    private static volatile Method set = null;
    private static volatile Method get = null;

    public static void set(String prop, String value) {
        try {
            if (null == set) {
                synchronized (PropertyUtils.class) {
                    if (null == set) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        set = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
                    }
                }
            }
            set.invoke(null, new Object[]{prop, value});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static String get(String prop, String defaultvalue) {
        String value = defaultvalue;
        try {
            if (null == get) {
                synchronized (PropertyUtils.class) {
                    if (null == get) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        get = cls.getDeclaredMethod("get", new Class<?>[]{String.class, String.class});
                    }
                }
            }
            value = (String) (get.invoke(null, new Object[]{prop, defaultvalue}));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
}
