package com.test.jm.service;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.util.CommonUtils;
import com.test.jm.util.RequestThreadLocal;
import com.test.jm.util.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestService {

    private static Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApiService apiService;

    public HttpClientResult request(ApiDTO apiDTO) throws Exception {
        logger.info("RequestService.request: {}",apiDTO.toString());
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
                logger.info("RequestService.replace.match: {}", match);
                CommonUtils.replaceCommon(apiDTO, match);
                CommonUtils.replacePre(apiDTO, match);
            }
        }
    }

    public List<HttpClientResult> runCase(String id){
        logger.info("RequestService.runCase.task_id: {}", id);
        TaskExtendDTO tt = new TaskExtendDTO();
        tt.setTask_id(id);
        tt.setStatus("1");
        List<TaskExtendDTO> data = taskService.getTaskExtendListById(tt);
        List<HttpClientResult> res = new LinkedList<>();
        for (TaskExtendDTO taskExtendDTO: data) {
            ApiDTO apiDTO = apiService.selectInterfaceById(taskExtendDTO.getApi_id());
            try {
                replaceFirst(apiDTO);
                HttpClientResult result = request(apiDTO);
                res.add(result);
                //后置处理
                String post_processors = taskExtendDTO.getPost_processors();
                Map<String, Object> map = RequestThreadLocal.getInfo();
                if(StringUtils.isNotBlank(post_processors)){
                    Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(post_processors).entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        System.out.println("key: " + entry.getKey() + ", value: "+ entry.getValue());
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
                continue;
            }
        }
        return res;
    }


}
