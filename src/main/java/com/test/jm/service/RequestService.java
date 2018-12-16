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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        replace(apiDTO);
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(),apiDTO.getBody(), apiDTO.getParamstype());
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(), apiDTO.getBody(), apiDTO.getParamstype());
                break;
            default:
                break;
        }
        return result;
    }

    private void replace(ApiDTO apiDTO){
        List<String> matchList = CommonUtils.getMatchList(apiDTO.getBody());
        if(null != matchList){
            //前置处理
            for (String match: matchList) {
                CommonUtils.replaceCommon(apiDTO, match);
            }
        }
    }

    public List<HttpClientResult> runCase(String id){
        logger.info("RequestService.runCase: {}", id);
        List<TaskExtendDTO> data = taskService.getTaskExtendById(id);
        List<HttpClientResult> res = new LinkedList<>();
        for (TaskExtendDTO taskExtendDTO: data) {
            ApiDTO apiDTO = apiService.selectInterfaceById(taskExtendDTO.getApi_id());
            try {
                replace(apiDTO);
                String pre = taskExtendDTO.getPre_processors();
                Map<String, Object> pre_search = CommonUtils.strToMap(pre);
                if(null != pre_search){
//                    String telno = (String) RequestThreadLocal.getInfo().get("telno");
                    System.out.println(222);
                }

                HttpClientResult result = request(apiDTO);
                res.add(result);

                //后置处理
                String post = taskExtendDTO.getPost_processors();
                if(StringUtils.isNotBlank(post)){
                    Map<String, Object> post_search = CommonUtils.strToMap(post);
                    Map<String, Object> map = new HashMap<>();
                    map.put("telno",result.getContent());
                    RequestThreadLocal.setInfo(map);
                }

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return res;
    }


}
