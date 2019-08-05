package com.ciba.data.gather.util;

import java.security.MessageDigest;

/**
 * Created by lenovo on 2017/9/6.
 */

public class MD5Util {
    public static String getMD5Code(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] data = digest.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                String result = Integer.toHexString(data[i] & 0xff);
                if (result.length() == 1) {
                    result = "0" + result;
                }
                sb.append(result);
            }
            return sb.toString();
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return null;
    }
}
