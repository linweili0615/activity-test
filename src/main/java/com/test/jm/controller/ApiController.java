package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.test.jm.domain.*;
import com.test.jm.domain.page.ApiPage;
import com.test.jm.domain.swagger.Group;
import com.test.jm.domain.swagger.Info;
import com.test.jm.domain.swagger.JsonSwagger;
import com.test.jm.domain.swagger.ReqInfo;
import com.test.jm.dto.APIvariables;
import com.test.jm.dto.ApiDTO;
import com.test.jm.dto.CaseDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.ApiService;
import com.test.jm.service.CaseService;
import com.test.jm.service.RequestService;
import com.test.jm.util.LogUtil;
import com.test.jm.util.UserThreadLocal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService apiService;

    @Autowired
    private CaseService caseService;

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

    @GetMapping("/variable_list")
    public ListResult getVariableList(){
        try {
            List<APIvariables> list = apiService.getAPIvariableList();
            return new ListResult(ResultType.SUCCESS,"获取变量列表成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ListResult(ResultType.ERROR,e.getMessage());
        }
    }

    @PostMapping("/all_list")
    public ApiResult getApiAllList(@RequestBody ApiPage apiPage){
        try {
            Integer pageSize = apiPage.getPageSize();
            Integer pageNo = apiPage.getPageNo();
            if(null == pageSize){
                pageSize = 50;
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

    @PostMapping("/swagger")
    public Result getSwaggerJson(@RequestBody ReqInfo reqInfo) {
        if(StringUtils.isBlank(reqInfo.getUrl()) || StringUtils.isBlank(reqInfo.getProject_id())){
            return new Result(ResultType.FAIL,"project_id或swagger接口地址不能为空");
        }
        if(!reqInfo.getUrl().endsWith("/v2/api-docs")){
            return new Result(ResultType.FAIL,"请输入正确的swagger接口地址");
        }
        String project_id = reqInfo.getProject_id();
        ApiDTO apiDTO = new ApiDTO();
        apiDTO.setUrl(reqInfo.getUrl());
        apiDTO.setMethod("GET");
        org.apache.logging.log4j.Logger log = LogUtil.getLogger("GET","12345678");
        try {
            HttpClientResult results = requestService.request(log, apiDTO);
            String res = results.getRes_body();
            Gson gson = new Gson();
            JsonSwagger swagger = gson.fromJson(res, JsonSwagger.class);
            String host = "http://" + swagger.getHost();
            List<Group> groupList = swagger.getTags();
            List<ApiDTO> apiDTOList = new ArrayList<>();
            List<CaseDTO> caseDTOList = new ArrayList<>();
            //获取分组信息
            for (Group group : groupList) {
                CaseDTO caseDTO = new CaseDTO();
                caseDTO.setProject_id(project_id);
                caseDTO.setName(group.getDescription());
                String case_id = caseService.addCase(caseDTO);
                caseDTO.setId(case_id);
                caseDTOList.add(caseDTO);
            }
            Map<String, Map<String,Info>> map = swagger.getPaths();
            //获取请求URIcaseDTOList
            for (String uri : map.keySet()) {
                ApiDTO api = new ApiDTO();
                Map<String,Info> mapmethod = map.get(uri);
                //获取请求方法
                for (String method : mapmethod.keySet()) {
                    Gson gsons = new Gson().newBuilder().enableComplexMapKeySerialization().create();
                    String jsonString = gsons.toJson(mapmethod.get(method));
                    Info info = gsons.fromJson(jsonString,Info.class);
                    api.setName(info.getSummary());
                    api.setMethod(method.toUpperCase());
                    if(method.toUpperCase().equals("POST")){
                        Map<String, String> header = new HashMap<>();
                        header.put("Content-Type","application/json");
                        api.setHeaders(gson.toJson(header));
                    }
                    api.setUrl(host + uri);
                    api.setParamstype("form");
                    System.out.println("project_id: "+ project_id);
                    api.setProject_id(project_id);
                    //获取所属分组
                    String tag = String.valueOf(info.getTags().get(0));
                    Optional<Group> group = groupList.stream().filter(gp -> gp.getName().equals(tag)).findFirst();
                    if (group.isPresent()){
                        System.out.println("group.isPresent()");
                        Group group1 = group.get();
                        Optional<CaseDTO> caseDTOOptional =  caseDTOList.stream().filter(cas -> cas.getName().equals(group1.getDescription())).findFirst();
                        if(caseDTOOptional.isPresent()){
                            System.out.println("caseDTOOptional.isPresent()");
                            CaseDTO cc = caseDTOOptional.get();
                            api.setCase_id(cc.getId());
                        }
                    }
                }
                apiService.addInterface(api);
                apiDTOList.add(api);
            }
            StringBuilder builder = new StringBuilder();
            builder.append("api信息已导入").append("\n");
            builder.append("分组数：").append(caseDTOList.size()).append("\n");
            builder.append("接口数：").append(apiDTOList.size()).append("\n");
            return new Result(ResultType.SUCCESS,builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(ResultType.ERROR,e.getMessage());
        }
    }

    @PostMapping("/test")
    public TestResult test_interface(@RequestBody ApiDTO apiDTO){
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult(ResultType.FAIL, "请求方法不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult(ResultType.FAIL, "请求URL不能为空");
        }

        try {
            org.apache.logging.log4j.Logger log = LogUtil.getLogger("GET","0");
            HttpClientResult result = requestService.request(log, apiDTO);
            if(result.getRes_code().equals(1000)){
                return new TestResult(ResultType.SUCCESS, result.getRes_body(), result);
            }
            return new TestResult(ResultType.SUCCESS, "", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new TestResult(ResultType.ERROR, e.getMessage());
        }


    }

    @PostMapping("/save")
    public TestResult save_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult(ResultType.FAIL, "项目ID不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult(ResultType.FAIL, "请求方法不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult(ResultType.FAIL, "请求URL不能为空");
        }

        try {
            if(StringUtils.isNotBlank(apiDTO.getId())){
                Integer count = apiService.editInterface(apiDTO);
                if(count > 0){
                    return new TestResult(apiDTO.getId(),ResultType.SUCCESS, "接口信息已更新");
                }else {
                    return new TestResult(ResultType.NOT_FOUND, "接口信息不存在");
                }
            }
            String id = apiService.addInterface(apiDTO);
            return new TestResult(id,ResultType.SUCCESS,"接口信息已保存");
        } catch (Exception e) {
            return new TestResult(ResultType.ERROR, e.getMessage());
        }

    }

    @PostMapping("/edit")
    public TestResult edit_interface(@RequestBody ApiDTO apiDTO) {
        if (StringUtils.isBlank(apiDTO.getId())) {
            return new TestResult(ResultType.FAIL, "接口ID不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getProject_id())) {
            return new TestResult(ResultType.FAIL, "项目ID不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getMethod())) {
            return new TestResult(ResultType.FAIL, "请求方法不能为空");
        }
        if (StringUtils.isBlank(apiDTO.getUrl())) {
            return new TestResult(ResultType.FAIL, "请求URL不能为空");
        }

        ApiDTO res = apiService.selectInterfaceById(apiDTO.getId());
        if (null != res) {
            try {
                Integer result = apiService.editInterface(apiDTO);
                if (result > 0) {
                    return new TestResult(ResultType.SUCCESS,"api信息已修改");
                }
                return new TestResult(ResultType.FAIL, "api信息修改失败");
            } catch (Exception e) {
                e.printStackTrace();
                return new TestResult(ResultType.ERROR, e.getMessage());
            }
        }
        return new TestResult(ResultType.NOT_FOUND, "接口ID不存在");


    }

    @PostMapping("/del")
    public TestResult removerInterfaceById(@RequestBody String id) {
        if (StringUtils.isBlank(id)) {
            return new TestResult(ResultType.FAIL, "接口ID不能为空");
        }

        try {
            Integer count = apiService.delInterfaceById(id);
            if(count > 0){
                return new TestResult(ResultType.SUCCESS, "记录已删除");
            }
            return new TestResult(ResultType.NOT_FOUND, "接口不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return new TestResult(ResultType.ERROR, e.getMessage());
        }
    }

    @PostMapping("/del/ids")
    public TestResult removerInterfaceIds(@RequestBody List<String> ids) {
        if (null == ids || ids.size() == 0 ) {
            return new TestResult(ResultType.FAIL, "接口ID集合不能为空");
        }
        try {
            Integer count = apiService.delApiByIds(ids);
            if(count > 0){
                return new TestResult(ResultType.SUCCESS, "记录已删除");
            }
            return new TestResult(ResultType.FAIL, "接口ID不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return new TestResult(ResultType.ERROR, e.getMessage());
        }
    }


}
