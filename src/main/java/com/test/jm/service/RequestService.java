package com.test.jm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.jm.domain.HttpClientResult;
import com.test.jm.domain.TaskResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RequestService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApiService apiService;

    public HttpClientResult request(ApiDTO apiDTO) throws Exception {
        HttpClientResult result = new HttpClientResult();
        replaceFirst(apiDTO);
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(),apiDTO.getBody(), apiDTO.getParamstype());
                setReqResult(result, apiDTO);
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(), apiDTO.getBody(), apiDTO.getParamstype());
                setReqResult(result, apiDTO);
                break;
            default:
                break;
        }
        return result;
    }

    private void setReqResult(HttpClientResult result, ApiDTO apiDTO){
        result.setApi_id(apiDTO.getId());
        result.setApi_name(apiDTO.getName());
        result.setReq_url(apiDTO.getUrl());
        result.setReq_method(apiDTO.getMethod());
    }

    private void replaceFirst(ApiDTO apiDTO){
        List<String> matchList = CommonUtils.getMatchList(apiDTO.getBody());
        if(null != matchList){
            //前置处理
            for (String match: matchList) {
                CommonUtils.replaceCommon(apiDTO, match);
                CommonUtils.replacePre(apiDTO, match);
            }
        }
    }

    public List<HttpClientResult> runCase(String id) throws IOException {
        org.apache.logging.log4j.Logger log = LogUtil.getLogger(id+ UserThreadLocal.getUserInfo().getUser_id());
        log.info("开始执行 task: {} ...",id);
        File file1 = new File("/result/"+id +".json");
        file1.deleteOnExit();

        TaskResult taskResult = new TaskResult();
        TaskExtendDTO tt = new TaskExtendDTO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss:SSS");
        Date start = new Date();
        taskResult.setStart_time(dateFormat.format(start));
        tt.setTask_id(id);
        tt.setStatus("1");
        List<TaskExtendDTO> data = taskService.getTaskExtendListById(tt);
        taskResult.setTotal(data.size());
        taskResult.setExecutor(UserThreadLocal.getUserInfo().getUser_name());
        Integer success = 0;
        Integer fail = 0;
        List<HttpClientResult> res = new LinkedList<>();
        for (TaskExtendDTO taskExtendDTO: data) {
            ApiDTO apiDTO = apiService.selectInterfaceById(taskExtendDTO.getApi_id());
            try {
                replaceFirst(apiDTO);
                Date date1 = new Date();
                log.info("{} 开始请求 ==> api: {} \n name: {} \n method: {} \n url: {}",dateFormat.format(date1),apiDTO.getId(),apiDTO.getName(),apiDTO.getMethod(),apiDTO.getUrl());
                HttpClientResult result = request(apiDTO);
                result.setStart_time(dateFormat.format(date1));
                Date date2 = new Date();
                log.info("{} 结束请求 ==> api: {}",dateFormat.format(date2),apiDTO.getId());
                result.setEnd_time(dateFormat.format(date2));
                res.add(result);

                if(result.getRes_code() == 1000){
                    fail++;
                }else {
                    success++;
                }

                //后置处理
                String post_processors = taskExtendDTO.getPost_processors();
                Map<String, Object> map = RequestThreadLocal.getInfo();
                if(StringUtils.isNotBlank(post_processors)){
                    Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(post_processors).entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        String pre = "";
                        String post = "";
                        String match = CommonUtils.regMatch(pre, post, result.getRes_body());
                        if(StringUtils.isNotBlank(match)){
                            map.put("telno", match);
                            RequestThreadLocal.setInfo(map);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("请求异常：api:{}\n{}",apiDTO.getId(),e.getMessage());
                continue;
            }
        }
        log.info("执行结束 task: {}",id);
        Date end = new Date();
        taskResult.setEnd_time(dateFormat.format(end));
        taskResult.setSuccess(success);
        taskResult.setFail(fail);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String percent = numberFormat.format((float) success / (float) taskResult.getTotal() * 100);
        taskResult.setPercent(percent);
        taskResult.setResultList(res);
        String jsonstr = JSONObject.toJSONString(taskResult);
        System.out.println(jsonstr);

        return res;
    }


}
