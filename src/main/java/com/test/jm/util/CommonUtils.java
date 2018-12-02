package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.jm.domain.Case;
import com.test.jm.domain.CaseTree;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.CaseDTO;
import org.apache.commons.lang.StringUtils;

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
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    public static List changeTree(List<CaseExtend> caseExtends){
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




}
