package com.hospital.utils;

import org.springframework.util.DigestUtils;

//Md5Util类提供了一个简单的方法来生成MD5哈希值，结合了盐值以提高安全性。适用于需要对敏感信息（如密码）进行加密存储的场景。

public class Md5Util {

    //盐，用于混交md5
    private static String salt = "asdwqAsd12_qS";

    public static String getMD5(String str) {
        String base = str + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


}