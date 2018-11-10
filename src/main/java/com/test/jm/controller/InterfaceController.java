package com.test.jm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.test.jm.domain.TestResult;
import com.test.jm.dto.test.InterfaceDTO;
import java.util.Iterator;
import java.util.Map.Entry;

import com.test.jm.service.InterfaceService;
import com.test.jm.util.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    private Logger logger = LoggerFactory.getLogger(InterfaceController.class);

    @Autowired
    private InterfaceService interfaceService;

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody InterfaceDTO interfaceDTO) throws IOException {
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(interfaceDTO.getMethod()) && StringUtils.isNotBlank(interfaceDTO.getUrl())){

            try {
                HttpResponse response = HttpClientUtils.post(interfaceDTO.getUrl(),interfaceDTO.getHeaders(),interfaceDTO.getBody());
                String response_status_code = response.getStatusLine().toString();
                String response_headers = HttpClientUtils.getResponseHeaders(response);
                String response_body = HttpClientUtils.getResponseBody(response);

                testResult.setStatus(response_status_code);
                testResult.setResheaders(response_headers);
                testResult.setResbody(response_body);
                return testResult;
            } catch (Exception e) {
                e.printStackTrace();
                testResult.setStatus("600");
                testResult.setResbody("请求异常");
                return testResult;
            }

        }
        testResult.setStatus("601");
        testResult.setResbody("请求方式或URL地址不能为空");
        return testResult;
    }

    @PostMapping("/save")
    public TestResult save_interface(@RequestBody InterfaceDTO interfaceDTO){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(interfaceDTO.getMethod()) && StringUtils.isNotBlank(interfaceDTO.getUrl())){
            InterfaceDTO info = new InterfaceDTO();
            info.setMethod(interfaceDTO.getMethod());
            info.setUrl(interfaceDTO.getUrl());
            info.setHeaders(interfaceDTO.getHeaders());
            info.setBody(interfaceDTO.getBody());

            try {
                String id = interfaceService.addInterface(info);
                testResult.setId(id);
                testResult.setStatus("200");
                testResult.setResbody("保存成功");
                return testResult;
            } catch (Exception e) {
                e.printStackTrace();
                testResult.setStatus("600");
                testResult.setResbody("保存异常");
                return testResult;
            }


        }
        testResult.setStatus("601");
        testResult.setResbody("请求方式或URL地址不能为空");
        return testResult;
    }

    @PostMapping("/edit")
    public TestResult edit_interface(@RequestBody InterfaceDTO interfaceDTO){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(interfaceDTO.getId())){
            Integer res = interfaceService.selectInterfaceById(interfaceDTO.getId());
            if(res > 0){
                if(StringUtils.isNotBlank(interfaceDTO.getMethod()) && StringUtils.isNotBlank(interfaceDTO.getUrl())){
                    InterfaceDTO info = new InterfaceDTO();
                    info.setId(interfaceDTO.getId());
                    info.setMethod(interfaceDTO.getMethod());
                    info.setUrl(interfaceDTO.getUrl());
                    info.setHeaders(interfaceDTO.getHeaders());
                    info.setBody(interfaceDTO.getBody());

                    try {
                        Integer result = interfaceService.editInterface(info);
                        if(result > 0 ){
                            testResult.setId(interfaceDTO.getId());
                            testResult.setStatus("200");
                            testResult.setResbody("保存成功");
                            return testResult;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        testResult.setStatus("600");
                        testResult.setResbody("保存异常");
                        return testResult;
                    }


                }
                testResult.setStatus("601");
                testResult.setResbody("请求方式或URL地址不能为空");
                return testResult;
            }
            testResult.setStatus("601");
            testResult.setResbody("请求接口id不存在");
            return testResult;

        }
        testResult.setStatus("601");
        testResult.setResbody("请求接口id不能为空");
        return testResult;

    }






}
