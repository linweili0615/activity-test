package com.test.jm.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {

    private static String SECRET = "linweili";

    public static String encoder(String pwd){
        String md5str = DigestUtils.md5Hex(SECRET.substring(0,3) + pwd + SECRET.substring(3,8));
        return md5str;
    }
}
