package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.jm.domain.*;
import com.test.jm.domain.page.ApiPage;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.ApiService;
import com.test.jm.service.RequestService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService apiService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/detail")
    public Result getApiList(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.FAIL,"id不能为空",null);
        }
        try {
            ApiDTO apiDTO = apiService.selectInterfaceById(id);
            if(null != apiDTO){
                return new Result(ResultType.SUCCESS,null,apiDTO);
            }
            return new Result(ResultType.NOT_FOUND,null, null);
        } catch (Exception e) {
            return new Result(ResultType.FAIL,e.getMessage(),null);
        }
    }

    @PostMapping("/all_list")
    public ApiResult getApiAllList(@RequestBody ApiPage apiPage){
        try {
            Integer pageSize = apiPage.getPageSize();
            Integer pageNo = apiPage.getPageNo();
            if(null == pageSize){
                pageSize = 70;
            }
            if(null == pageNo){
                pageNo = 1;
            }
            PageHelper.startPage(pageNo,pageSize);
            List<ApiDTO> apiDTOList = apiService.getApiList(apiPage);
            PageInfo<ApiDTO> pageInfo = new PageInfo<>(apiDTOList);
            int row_count = (int) pageInfo.getTotal();
            int pageCount = row_count % pageSize==0 ? row_count/pageSize : row_count/pageSize + 1;

            if(null != apiDTOList){
                return new ApiResult(ResultType.SUCCESS, "获取api列表成功", pageInfo.getTotal(), pageSize, pageCount, pageNo, apiDTOList);
            }
            return new ApiResult(ResultType.SUCCESS, "获取api列表为空", null);
        } catch (Exception e) {
//            e.printStackTrace();
            return new ApiResult(ResultType.FAIL, e.getMessage(), null);
        }
    }

    @PostMapping("/list")
    public ApiResult getApiList(@RequestBody ApiPage apiPage){
        if (StringUtils.isBlank(apiPage.getProject_id())) {
            return new ApiResult(ResultType.FAIL, "所属项目不能为空", null);
        }
        try {
            Integer pageSize = apiPage.getPageSize();
            Integer pageNo = apiPage.getPageNo();
            if(null == pageSize){
                pageSize = 30;
            }
            if(null == pageNo){
                pageNo = 1;
            }
            PageHelper.startPage(pageNo,pageSize);
            List<ApiDTO> apiDTOList = apiService.getApiList(apiPage);
            PageInfo<ApiDTO> pageInfo = new PageInfo<>(apiDTOList);
            int row_count = (int) pageInfo.getTotal();
            int pageCount = row_count % pageSize==0 ? row_count/pageSize : row_count/pageSize + 1;

            if(null != apiDTOList){
                return new ApiResult(ResultType.SUCCESS, "获取api列表成功", pageInfo.getTotal(), pageSize, pageCount, pageNo, apiDTOList);
            }
            return new ApiResult(ResultType.SUCCESS, "获取api列表为空", null);
        } catch (Exception e) {
//            e.printStackTrace();
            return new ApiResult(ResultType.FAIL, e.getMessage(), null);
        }
    }

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody ApiDTO apiDTO){
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult(null, ResultType.FAIL, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult(null, ResultType.FAIL, "请求URL不能为空", null);
        }

        try {
            HttpClientResult result = requestService.request(apiDTO);
            if(result.getRes_code().equals(1000)){
                return new TestResult(null, ResultType.SUCCESS, result.getRes_body(), result);
            }
            return new TestResult(null, ResultType.SUCCESS, "", result);
        } catch (Exception e) {
            return new TestResult(null, ResultType.ERROR, e.getMessage(), null);
        }


    }

    @PostMapping("/save")
    public TestResult save_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult("", ResultType.NOT_FOUND, "项目ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult("", ResultType.NOT_FOUND, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult("", ResultType.NOT_FOUND, "请求URL不能为空", null);
        }

        try {
            if(StringUtils.isNotBlank(apiDTO.getId())){
                Integer count = apiService.editInterface(apiDTO);
                if(count > 0){
                    return new TestResult(apiDTO.getId(), ResultType.SUCCESS, "接口信息已更新", null);
                }else {
                    return new TestResult(apiDTO.getId(), ResultType.NOT_FOUND, "接口信息不存在", null);
                }
            }
            String id = apiService.addInterface(apiDTO);
            return new TestResult(id, ResultType.SUCCESS, "接口信息已保存", null);
        } catch (Exception e) {
            return new TestResult(null, ResultType.ERROR, e.getMessage(), null);
        }

    }

    @PostMapping("/edit")
    public TestResult edit_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getId())) {
            return new TestResult("", ResultType.NOT_FOUND, "接口ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult("", ResultType.NOT_FOUND, "项目ID不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult("", ResultType.NOT_FOUND, "请求方法不能为空", null);
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult("", ResultType.NOT_FOUND, "请求URL不能为空", null);
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
//                e.printStackTrace();
                return new TestResult(apiDTO.getId(), ResultType.ERROR, e.getMessage(), null);
            }
        }
        return new TestResult(apiDTO.getId(), ResultType.NOT_FOUND, "接口ID不存在", apiDTO);


    }

    @PostMapping("/del")
    public TestResult removerInterfaceById(@RequestBody String id) {
        if (StringUtils.isBlank(id)) {
            return new TestResult("", ResultType.NOT_FOUND, "接口ID不能为空", null);
        }

        try {
            Integer count = apiService.delInterfaceById(id);
            if(count > 0){
                return new TestResult(id, ResultType.SUCCESS, "记录已删除", null);
            }
            return new TestResult(id, ResultType.NOT_FOUND, "接口不存在", null);
        } catch (Exception e) {
//            e.printStackTrace();
            return new TestResult(id, ResultType.ERROR, e.getMessage(), null);
        }
    }


}
