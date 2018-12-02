package com.test.jm.controller;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.domain.TestResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.ApiService;
import com.test.jm.service.RequestService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService apiService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody ApiDTO apiDTO){
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult("", ResultType.FAIL, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult("", ResultType.FAIL, "请求URL不能为空", null);
        }

        try {
            HttpClientResult result = requestService.request(apiDTO);
            return new TestResult("", ResultType.SUCCESS, "", result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new TestResult("", ResultType.ERROR, e.getMessage(), null);
        }


    }

    @PostMapping("/save")
    public TestResult save_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult("", ResultType.FAIL, "项目ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult("", ResultType.FAIL, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult("", ResultType.FAIL, "请求URL不能为空", null);
        }

        try {
            String id = apiService.addInterface(apiDTO);
            return new TestResult(id, ResultType.SUCCESS, "", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new TestResult("", ResultType.ERROR, "保存异常", null);
        }

    }

    @PostMapping("/edit")
    public TestResult edit_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getId())) {
            return new TestResult("", ResultType.FAIL, "接口ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult("", ResultType.FAIL, "项目ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult("", ResultType.FAIL, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult("", ResultType.FAIL, "请求URL不能为空", null);
        }

        ApiDTO res = apiService.selectInterfaceById(apiDTO.getId());
        if (null != res) {
            try {
                Integer result = apiService.editInterface(apiDTO);
                if (result > 0) {
                    return new TestResult(apiDTO.getId(), ResultType.SUCCESS, "", null);
                }
                return new TestResult(apiDTO.getId(), ResultType.FAIL, "修改失败", "");
            } catch (Exception e) {
                e.printStackTrace();
                return new TestResult(apiDTO.getId(), ResultType.ERROR, "修改异常", null);
            }
        }
        return new TestResult(apiDTO.getId(), ResultType.FAIL, "接口ID不存在", apiDTO);


    }

    @PostMapping("/del")
    public TestResult removerInterfaceById(@RequestBody String id) {
        if (StringUtils.isBlank(id)) {
            return new TestResult("", ResultType.FAIL, "接口ID不能为空", null);
        }

        if (null != apiService.selectInterfaceById(id)) {
            try {
                Integer count = apiService.delInterfaceById(id);
                if(count > 0){
                    return new TestResult(id, ResultType.SUCCESS, "", null);
                }
                return new TestResult(id, ResultType.FAIL, "删除失败", null);
            } catch (Exception e) {
                e.printStackTrace();
                return new TestResult(id, ResultType.ERROR, "删除异常", null);
            }
        }
        return new TestResult(id, ResultType.FAIL, "接口ID不存在", null);
    }


}
