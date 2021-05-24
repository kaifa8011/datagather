package com.ciba.data.gather.util;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lizhlin83 on 2017/12/27.
 */
public class PrivatePC {
    private static final String PRIVATE_KEY = "MIGfMA0GCSqGSIb3";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public String decrypt(String content) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            SecretKey key = new SecretKeySpec(PRIVATE_KEY.getBytes(), "AES");
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, key);

            //执行操作
            byte[] result = cipher.doFinal(decryptBASE64(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
        }

        return null;
    }

    public static byte[] decryptBASE64(String key) {
        return Base64.decode(key.trim(), Base64.DEFAULT);
    }
}
