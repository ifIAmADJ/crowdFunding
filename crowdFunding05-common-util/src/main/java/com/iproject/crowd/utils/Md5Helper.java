package com.iproject.crowd.utils;

import com.iproject.crowd.constant.ProjectConstant;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Helper {

    /**
     * 通用的字符串加密方法。
     * @param plain 输入被加密的字符串明文。
     * @return 返回被加密的结果。
     */
    public static String md5(String plain) {

        // 1. 判断 source 是否有效。
        if (plain == null || plain.length() == 0) {
            throw new RuntimeException(ProjectConstant.MESSAGE_STRING_INVALID);
        }

        try {

            // 2. 获取 MessageDigest 对象, java.security 直接提供。
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 3. 获取铭文字符串对应的字节数组
            byte[] input = plain.getBytes();
            byte[] output = messageDigest.digest(input);

            int signum = 1;
            int radix = 16;

            // 4. 最终返回的数值.
            return new BigInteger(signum,output).toString(radix).toUpperCase();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }


        return null;

    }
}
