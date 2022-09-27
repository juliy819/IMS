package com.juliy.ims.utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 * @author juliy
 * @date 2022/9/27 14:00
 */
public class Md5Util {

    /** 16进制数字集 */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 将byte数组转换为16进制字符串
     * @param b byte数组
     * @return 转换后的16进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * 将byte转换为16进制字符串
     * @param b 要转换的byte
     * @return 转换后的16进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 对字符串进行MD5加密
     * @param origin      要加密的字符串
     * @param charsetName 编码格式
     * @return 加密后的32位字符串
     */
    public static String md5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
