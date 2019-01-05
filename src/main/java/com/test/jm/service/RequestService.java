package com.test.jm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.jm.domain.HttpClientResult;
import com.test.jm.domain.TaskResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RequestService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApiService apiService;

    //替换变量属性
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

    private void setReqResult(HttpClientResult result, ApiDTO apiDTO){
        result.setApi_id(apiDTO.getId());
        result.setApi_name(apiDTO.getName());
        result.setReq_method(apiDTO.getMethod());
    }

    public HttpClientResult normal_request(ApiDTO apiDTO) throws Exception {
        HttpClientResult result = new HttpClientResult();
        replaceFirst(apiDTO);
        setReqResult(result, apiDTO);
        Logger log = LogUtil.getLogger("12345678");
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(log, result, apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(),apiDTO.getBody(), apiDTO.getParamstype());
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(log, result, apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(), apiDTO.getBody(), apiDTO.getParamstype());
                break;
            default:
                break;
        }
        return result;
    }

    public HttpClientResult request(Logger logger,ApiDTO apiDTO) throws Exception {
        HttpClientResult result = new HttpClientResult();
        replaceFirst(apiDTO);
        setReqResult(result, apiDTO);
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(logger, result, apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(),apiDTO.getBody(), apiDTO.getParamstype());
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(logger, result, apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(), apiDTO.getBody(), apiDTO.getParamstype());
                break;
            default:
                break;
        }
        return result;
    }

    public List<HttpClientResult> runCase(String id) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Logger log = LogUtil.getLogger(id);
        log.info("开始执行 TASK: {} ...",id);
        TaskResult taskResult = new TaskResult();
        TaskExtendDTO tt = new TaskExtendDTO();
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
                log.info("{} 开始请求 ==> api: {}",dateFormat.format(date1),apiDTO.getId());
                HttpClientResult result = request(log, apiDTO);
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
        log.info(dateFormat.format(new Date()) + "执行结束 TASK: {}",id);
        Date end = new Date();
        taskResult.setEnd_time(dateFormat.format(end));
        taskResult.setSuccess(success);
        taskResult.setFail(fail);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String percent = numberFormat.format((float) success / (float) taskResult.getTotal() * 100);
        taskResult.setPercent(percent);
        taskResult.setResultList(res);

        String fullPath = "task/" + File.separator +id +  File.separator + UserThreadLocal.getUserInfo().getUser_id() +   File.separator + "result.json";
        File file = new File(fullPath);
        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            file.getParentFile().mkdirs();
        }
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();
        // 将格式化后的字符串写入文件
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonstr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskResult);
        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        write.write(jsonstr);
        write.flush();
        write.close();
        //写入执行结果
        return res;
    }


}
