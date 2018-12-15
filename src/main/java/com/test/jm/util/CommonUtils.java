package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.jm.domain.tree.ApiCase;
import com.test.jm.domain.tree.ApiTree;
import com.test.jm.domain.tree.Case;
import com.test.jm.domain.tree.CaseTree;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.CaseDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.*;

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
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    public static String HeaderListToMap(List<Header> lists){
        Map<String, Object> m = new HashMap<>();
        try {
            for (Header header: lists) {
                m.put(header.getName(), header.getValue());
            }
            JSONObject json = new JSONObject(m);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return lists.toString();
        }

    }

    public static String CookieListToMap(List<Cookie> lists){
        Map<String, Object> m = new HashMap<>();
        try {
            for (Cookie cookie: lists) {
                m.put(cookie.getName(), cookie.getValue());
            }
            JSONObject json = new JSONObject(m);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return lists.toString();
        }

    }

    public static List caseTree(List<CaseExtend> caseExtends){
        List all = new LinkedList();
        for (int i = 0; i < caseExtends.size(); i++) {
            CaseExtend cc = caseExtends.get(i);
            CaseTree caseTree = new CaseTree();
            caseTree.setId(cc.getPid());
            caseTree.setLabel(cc.getProject_name());
            List cd = new ArrayList<>();
            for (CaseDTO caseDTO:cc.getCaseExtends()) {
                Case c = new Case();
                c.setId(caseDTO.getId());
                c.setLabel(caseDTO.getName());
                cd.add(c);
            }
            caseTree.setChildren(cd);
            all.add(caseTree);
        }

        return all;
    }

    public static List apiTree(List<CaseExtend> caseExtends){
        List all = new LinkedList();
        for (int i = 0; i < caseExtends.size(); i++) {
            CaseExtend cc = caseExtends.get(i);
            ApiTree apiTree = new ApiTree();
            apiTree.setValue(cc.getPid());
            apiTree.setLabel(cc.getProject_name());
            List cd = new ArrayList<>();
            for (CaseDTO caseDTO:cc.getCaseExtends()) {
                ApiCase c = new ApiCase();
                c.setValue(caseDTO.getId());
                c.setLabel(caseDTO.getName());
                cd.add(c);
            }
            apiTree.setChildren(cd);
            all.add(apiTree);
        }

        return all;
    }




}
