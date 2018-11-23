package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

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

    public static Map<String, Object> strToMap(String str){
        if(StringUtils.isNotBlank(str)){
            Map<String, Object> map = JSONObject.parseObject(str);
            return map;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }




}
