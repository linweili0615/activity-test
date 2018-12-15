package com.test.jm.util;

import java.util.Map;

public class RequestThreadLocal {

    private static ThreadLocal<Map<String,Object>> info = new ThreadLocal<>();

    public static Map<String,Object> getInfo() {
        return info.get();
    }

    public static void setInfo(Map<String,Object> map) {
        info.set(map);
    }
}
