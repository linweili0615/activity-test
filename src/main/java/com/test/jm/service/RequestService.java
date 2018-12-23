package com.test.jm.service;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.util.CommonUtils;
import com.test.jm.util.LogUtil;
import com.test.jm.util.RequestThreadLocal;
import com.test.jm.util.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<HttpClientResult> runCase(String id){
        org.apache.logging.log4j.Logger log = LogUtil.getLogger(id);
        log.info("开始执行 task: {} ...",id);
        TaskExtendDTO tt = new TaskExtendDTO();
        tt.setTask_id(id);
        tt.setStatus("1");
        List<TaskExtendDTO> data = taskService.getTaskExtendListById(tt);
        List<HttpClientResult> res = new LinkedList<>();
        for (TaskExtendDTO taskExtendDTO: data) {
            ApiDTO apiDTO = apiService.selectInterfaceById(taskExtendDTO.getApi_id());
            try {
                replaceFirst(apiDTO);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss:SSS");
                Date date1 = new Date();
                log.info("{} 开始请求 ==> api: {} \n name: {} \n method: {} \n url: {}",dateFormat.format(date1),apiDTO.getId(),apiDTO.getName(),apiDTO.getMethod(),apiDTO.getUrl());
                HttpClientResult result = request(apiDTO);
                Date date2 = new Date();
                log.info("{} 结束请求 ==> api: {}",dateFormat.format(date2),apiDTO.getId());
                res.add(result);
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
        return res;
    }


}
