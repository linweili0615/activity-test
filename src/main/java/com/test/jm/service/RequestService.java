package com.test.jm.service;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.domain.TaskResult;
import com.test.jm.dto.ApiDTO;
import com.test.jm.dto.TaskCaseLog;
import com.test.jm.dto.TaskDrawDTO;
import com.test.jm.dto.TaskExtendDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.keys.TaskType;
import com.test.jm.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RequestService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskCaseLogService logService;

    @Autowired
    private TaskDrawService taskDrawService;

    @Autowired
    private ApiService apiService;

    //前置处理
    private void replaceFirst(ApiDTO apiDTO){
        //提取params
        List<String> matchList = CommonUtils.getMatchList(apiDTO.getBody());
        if(null != matchList && matchList.size()>0){
            for (String match: matchList) {
                CommonUtils.replaceCommon("param", apiDTO, match);
            }
        }
        //提取url
        List<String> matchList1 = CommonUtils.getMatchList(apiDTO.getUrl());
        if(null != matchList1 && matchList1.size()>0){
            for (String match: matchList1) {
                CommonUtils.replaceCommon("url", apiDTO, match);
            }
        }
    }
    //后置处理
    private void replacePost(HttpClientResult result, TaskDrawDTO taskDrawDTO){
        String match = CommonUtils.regMatch(taskDrawDTO.getLeft(),taskDrawDTO.getRight(),result.getRes_body());
        System.out.println("match: "+match);
        if(StringUtils.isNotBlank(match)){
            Map<String,Object> info = RequestThreadLocal.getInfo();
            if(null ==info){
                info = new HashMap<>();
            }
            info.put(taskDrawDTO.getValues(),match);
            RequestThreadLocal.setInfo(info);
        }
    }

    private void setReqResult(HttpClientResult result, ApiDTO apiDTO){
        result.setApi_id(apiDTO.getId());
        result.setApi_name(apiDTO.getName());
        result.setReq_method(apiDTO.getMethod());
    }

    public HttpClientResult request(Logger logger,ApiDTO apiDTO) throws Exception {
        HttpClientResult result = new HttpClientResult();
        replaceFirst(apiDTO);
        setReqResult(result, apiDTO);
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(logger, result, apiDTO);
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(logger, result, apiDTO);
                break;
            default:
                break;
        }
        return result;
    }

    public Map<String, Object> getLog(String log_type, String run_type, String task_id){
        Map<String, Object> map = new HashMap<>();
        TaskCaseLog caseLog = null;
        String u_id = UserThreadLocal.getUserInfo().getUser_id();
        if(run_type.equals(TaskType.TEST)){
            caseLog = new TaskCaseLog(task_id,u_id);
        }else {
            caseLog = new TaskCaseLog(task_id,task_id);
        }
        TaskCaseLog taskCaseLog = logService.getTaskCaseLog(caseLog);
        if(null == taskCaseLog){
            Integer log_id = logService.insertTaskCaseLog(caseLog);
            Logger log = LogUtil.getLogger(log_type,log_id.toString());
            map.put("log_id",log_id);
            map.put("log",log);
        }else {
            Logger log = LogUtil.getLogger(log_type,taskCaseLog.getId().toString());
            map.put("log_id",taskCaseLog.getId());
            map.put("log",log);
        }
        return map;
    }

    public List<HttpClientResult> runCase(String run_type, String task_id) throws IOException {
        logger.info("runCase: {}",task_id);
        Map<String, Object> task_map = getLog("WRITE",run_type, task_id);
        Logger log = (Logger) task_map.get("log");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        log.info("开始执行 TASK: {} ...",task_id);
        TaskResult taskResult = new TaskResult();
        TaskExtendDTO tt = new TaskExtendDTO();
        Date start = new Date();
        taskResult.setStart_time(dateFormat.format(start));
        tt.setTask_id(task_id);
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
                Date date1 = new Date();
                log.info("{} 开始请求 ==> api: {}",dateFormat.format(date1),apiDTO.getId());
                HttpClientResult result = request(log, apiDTO);
                result.setStart_time(dateFormat.format(date1));
                Date date2 = new Date();
                log.info("{} 结束请求 ==> api: {}",dateFormat.format(date2),apiDTO.getId());
                result.setEnd_time(dateFormat.format(date2));
                res.add(result);
                if(result.getRes_code() == 1000){
                    result.setResult("失败");
                    fail++;
                }else {
                    result.setResult("通过");
                    success++;
                }
                //后置处理

                List<TaskDrawDTO> taskDrawDTOList = taskDrawService.getTaskDrawById(taskExtendDTO.getId());
                for (TaskDrawDTO drawDTO:taskDrawDTOList) {
                    replacePost(result,drawDTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("请求异常：api:{}\n{}",apiDTO.getId(),e.getMessage());
                continue;
            }
        }
        log.info("执行结束 TASK: {}",task_id);
        Date end = new Date();
        long interval = ( end.getTime() - start.getTime())/1000;
        taskResult.setConsuming_time(interval);
        taskResult.setEnd_time(dateFormat.format(end));
        taskResult.setSuccess(success);
        taskResult.setFail(fail);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String percent = numberFormat.format((float) success / (float) taskResult.getTotal() * 100);
        taskResult.setPercent(percent);
        taskResult.setResultList(res);

        String fullPath = "task/" + File.separator +task_map.get("log_id") +  File.separator  + "result.json";
        File file = new File(fullPath);
        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            file.getParentFile().mkdirs();
        }
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();
        // 将格式化后的字符串写入文件
        String jsonstr = CommonUtils.ObjectToJsonStr(taskResult);
        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        write.write(jsonstr);
        write.flush();
        write.close();
        //写入执行结果
        logger.info("runCase end: {}",task_id);
        return res;
    }


}
