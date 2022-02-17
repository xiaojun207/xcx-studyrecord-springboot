package com.tencent.wxcloudrun.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtils {
    private static final String strType = "SHA-256";

    /**
     * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     */
    public static String md5(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));
            // 将字节数据转换为十六进制
            for (byte b : md5) {
                sb.append(Integer.toHexString(b & 0xff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


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

