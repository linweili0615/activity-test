package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

public class JSONUtils {

    public static boolean isJson(String content) {
        if(StringUtils.isBlank(content)){
            return false;
        }
        try {
            JSONObject jsonObj = JSON.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
