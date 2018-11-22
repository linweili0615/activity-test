package com.test.jm.util;

import com.test.jm.dto.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OKHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OKHttpUtil.class);

    public static String get(String url, Map<String, String> queries){

        StringBuffer buffer = new StringBuffer();
        if(queries != null && queries.keySet().size() > 0){
            boolean flag = true;
            List<Header> list = new ArrayList<Header>();
            list.add(new Header("k1","v1"));
            list.add(new Header("k2","v3"));
            list.stream().forEach(x -> {
                System.out.println(x.getKey() + " , "+ x.getValue());
            });

        }

        return null;
    }



}
