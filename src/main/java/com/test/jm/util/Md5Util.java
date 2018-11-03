package com.test.jm.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {

    private static final String KEY = "linweili";

    public static String encoder(String text) {

        String encodeStr = DigestUtils.md5Hex(KEY.substring(3) + text + KEY.substring(3,8));
        return encodeStr;
    }

    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5Text = encoder(text);
        if(md5Text.equalsIgnoreCase(md5))
        {
            return true;
        }

        return false;
    }
}
