package com.tencent.wxcloudrun.utils;

import java.security.MessageDigest;

public class SHAUtils {
    private static final String strType = "SHA-256";

    /***
     * 字符串 SHA 加密**
     * @param strText
     * @return
     */
    public static String SHA(final String strText) {
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果,转换为字符串
                return new String(messageDigest.digest());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

}

