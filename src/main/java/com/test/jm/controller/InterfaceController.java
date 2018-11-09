package com.test.jm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.test.jm.domain.TestResult;
import com.test.jm.dto.test.InterfaceDTO;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody InterfaceDTO interfaceDTO) throws IOException {
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(interfaceDTO.getMethod()) && StringUtils.isNotBlank(interfaceDTO.getUrl())){
            System.out.println(interfaceDTO.getHeaders());
//            new JsonObject().getAsJsonObject(interfaceDTO.getHeaders()).keySet();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(interfaceDTO.getHeaders());
            Iterator<Entry<String, JsonNode>> elements = node.fields();
            while (elements.hasNext()) {
                Map.Entry<String, JsonNode> element = elements.next();
                String key = element.getKey();
                String value = element.getValue().toString();
                System.out.println(new StringBuilder().append("key: ").append(key).append(" ,value: ").append(value));
            }
            testResult.setId("");
            testResult.setStatus(200);
            testResult.setTest_result("请求成功");
            return testResult;
        }
        testResult.setStatus(601);
        testResult.setTest_result("请求方式或URL地址不能为空");
        return testResult;
    }

    @PostMapping("/save")
    public TestResult save_interface(@RequestBody InterfaceDTO interfaceDTO){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(interfaceDTO.getMethod()) && StringUtils.isNotBlank(interfaceDTO.getUrl())){
            testResult.setStatus(200);
            testResult.setTest_result("保存成功");
        }
        testResult.setStatus(601);
        testResult.setTest_result("请求方式或URL地址不能为空");
        return testResult;
    }





}
