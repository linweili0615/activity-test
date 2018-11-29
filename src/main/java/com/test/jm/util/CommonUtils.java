package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.jm.domain.CaseTree;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.CaseDTO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static CaseTree changeTree(List<CaseExtend> caseExtends){
        CaseExtend cc = caseExtends.get(0);
        CaseTree caseTree = new CaseTree();
        caseTree.setId(cc.getPid());
        caseTree.setName(cc.getProject_name());
        List<CaseDTO> cd = new ArrayList<>();
        for (CaseDTO caseDTO:cc.getCaseExtends()) {
            cd.add(caseDTO);
        }
        caseTree.setChildren(cd);
        return caseTree;
    }




}
