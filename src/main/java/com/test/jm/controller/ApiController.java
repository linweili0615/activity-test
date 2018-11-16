package com.test.jm.controller;

import com.test.jm.domain.TestResult;
import com.test.jm.dto.test.ApiDTO;

import com.test.jm.service.ApiService;
import com.test.jm.util.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService apiService;

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody ApiDTO apiDTO) throws IOException {
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(apiDTO.getMethod()) && StringUtils.isNotBlank(apiDTO.getUrl())){

            try {
                System.out.println("url: " + apiDTO.getUrl());
                System.out.println("headers: " + apiDTO.getHeaders());
                System.out.println("body: " + apiDTO.getBody());
                HttpResponse response = HttpClientUtils.post(apiDTO.getUrl(), apiDTO.getHeaders(), apiDTO.getBody());
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
    public TestResult save_interface(@RequestBody ApiDTO apiDTO){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(apiDTO.getMethod()) && StringUtils.isNotBlank(apiDTO.getUrl())){
            ApiDTO info = new ApiDTO();
            info.setMethod(apiDTO.getMethod());
            info.setUrl(apiDTO.getUrl());
            info.setHeaders(apiDTO.getHeaders());
            info.setBody(apiDTO.getBody());

            try {
                String id = apiService.addInterface(info);
                if(StringUtils.isNotBlank(id)){
                    testResult.setId(id);
                    testResult.setStatus("200");
                    testResult.setResbody("保存成功");
                    return testResult;
                }
                testResult.setStatus("600");
                testResult.setResbody("保存失败");
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
    public TestResult edit_interface(@RequestBody ApiDTO apiDTO){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(apiDTO.getId())){
            Integer res = apiService.selectInterfaceById(apiDTO.getId());
            if(res > 0){
                if(StringUtils.isNotBlank(apiDTO.getMethod()) && StringUtils.isNotBlank(apiDTO.getUrl())){
                    ApiDTO info = new ApiDTO();
                    info.setId(apiDTO.getId());
                    info.setMethod(apiDTO.getMethod());
                    info.setUrl(apiDTO.getUrl());
                    info.setHeaders(apiDTO.getHeaders());
                    info.setBody(apiDTO.getBody());

                    try {
                        Integer result = apiService.editInterface(info);
                        if(result > 0 ){
                            testResult.setId(apiDTO.getId());
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

    @PostMapping("/del")
    public TestResult removerInterfaceById(@RequestParam(value = "id",required = true) String id){
        TestResult testResult = new TestResult();
        if(StringUtils.isNotBlank(id)) {
            if (apiService.selectInterfaceById(id) > 0) {
                if(apiService.delInterfaceById(id) > 0){
                    testResult.setId(id);
                    testResult.setStatus("200");
                    testResult.setResbody("接口删除成功");
                    return testResult;
                };
                testResult.setId(id);
                testResult.setStatus("600");
                testResult.setResbody("接口删除失败");
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
