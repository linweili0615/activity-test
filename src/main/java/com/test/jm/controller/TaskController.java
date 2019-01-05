package com.test.jm.controller;

import com.test.jm.domain.*;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.RequestService;
import com.test.jm.service.TaskService;
import com.test.jm.util.RedisUtil;
import com.test.jm.util.UserThreadLocal;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/task")
@RestController
public class TaskController {

    //超时时间 40秒
    private static final int TIMEOUT = 40 * 1000;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/getLog")
    public Result getlog(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.ERROR,"taskid不能为空",null);
        }
        String logname = new File("").getAbsolutePath() + "/task/" + id +"/" + UserThreadLocal.getUserInfo().getUser_id()+"/task.log";
        try {
            File file = new File(logname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s = "";
            List list = new ArrayList();
            while ((s = bufferedReader.readLine()) != null){
                if(StringUtils.isNotBlank(s.trim())){
                    list.add(s);
                }
            }
            reader.close();
            return new Result(ResultType.SUCCESS,null,list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Result(ResultType.NOT_FOUND,e.getMessage(),null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage(),null);
        }
    }

    @GetMapping("/getResult")
    public Result getresult(){
        String id = "81598efb-ffa9-11e8-a19c-0242ac110002";
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.FAIL,"taskid不能为空",null);
        }
        String fullPath = "task/" + File.separator +id +  File.separator + UserThreadLocal.getUserInfo().getUser_id() +   File.separator + "result.json";
        try {
            File file = new File(fullPath);
            if(!file.exists()){
                return new Result(ResultType.NOT_FOUND,"该任务结果不存在",null);
            }
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s = "";
            StringBuffer buffer = new StringBuffer();
            while ((s = bufferedReader.readLine()) != null){
                if(StringUtils.isNotBlank(s.trim())){
                    buffer.append(s);
                }
            }
            reader.close();
            return new Result(ResultType.SUCCESS,null,buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Result(ResultType.NOT_FOUND,e.getMessage(),null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage(),null);
        }
    }

    @RequestMapping("/test")
    public TaskExtendResult runCase(@RequestBody String task_id){
        if(StringUtils.isBlank(task_id)){
            return new TaskExtendResult(ResultType.FAIL, "task_id不能为空");
        }
//        long time = System.currentTimeMillis() + TIMEOUT;
//        String key = task_id + UserThreadLocal.getUserInfo().getUser_id();
//        if(!redisUtil.lock(key, String.valueOf(time))){
//            return new TaskExtendResult(ResultType.FAIL, "操作太频繁了哈");
//        }
        try {

            List<HttpClientResult> list = requestService.runCase(task_id);
            if(null != list){
                return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行成功", list);
            }
            return new TaskExtendResult(task_id, ResultType.FAIL, "task执行失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(task_id, ResultType.ERROR, "task执行异常", null);
        }finally {
//            redisUtil.unlock(key, String.valueOf(time));
        }
    }

    @PostMapping("/extend/info")
    public Map getTaskInfo(@RequestBody String id){
        Map<String,Object> resultMap = new HashMap<>();
        if(StringUtils.isBlank(id)){
            resultMap.put("msg", "任务ID不能为空");
            resultMap.put("status", ResultType.FAIL);
            resultMap.put("data",null);
            return resultMap;
        }
        try {
            TaskExtendDTO taskExtendDTO = new TaskExtendDTO();
            taskExtendDTO.setTask_id(id);
            List<TaskExtendDTO> data = taskService.getTaskExtendListById(taskExtendDTO);
            if(null == data){
                resultMap.put("id",id);
                resultMap.put("msg", "任务详情无记录");
                resultMap.put("status", ResultType.FAIL);
                resultMap.put("data",null);
                return resultMap;
            }
            resultMap.put("id",id);
            resultMap.put("msg", "获取任务详情成功");
            resultMap.put("status", ResultType.SUCCESS);
            resultMap.put("data",data);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("id",id);
            resultMap.put("msg", e.getMessage());
            resultMap.put("status", ResultType.ERROR);
            resultMap.put("data",null);
            return resultMap;
        }
    }

    @PostMapping("/extend/status")
    public TaskExtendResult modifyTaskExtendStatus(@RequestBody TaskExtendStatusParams params){
        if(null == params.getList() && params.getList().size() ==0){
            return new TaskExtendResult(null, ResultType.FAIL, "任务ID不能为空", null);
        }
        try {
            Integer count = taskService.updateTaskExtendStatusList(params);
            if(count > 0){
                return new TaskExtendResult(null, ResultType.SUCCESS, "更新任务集合status成功", null);
            }
            return new TaskExtendResult(null, ResultType.FAIL, "更新任务集合status失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(null, ResultType.ERROR, e.getMessage(), null);
        }

    }

    @PostMapping("/extend/deal")
    public TaskExtendResult dealTaskExtend(@RequestBody TaskExtendRank taskExtendRank){
        if(null == taskExtendRank){
            return new TaskExtendResult(null, ResultType.FAIL, "任务详情信息不能为空", null);
        }
        List<TaskExtendDTO> list = new ArrayList<>();
        TaskExtendDTO oldTaskExtend = new TaskExtendDTO();
        TaskExtendDTO newTaskExtend = new TaskExtendDTO();
        oldTaskExtend.setId(taskExtendRank.getOld_id());
        oldTaskExtend.setRank(taskExtendRank.getNew_rank());
        newTaskExtend.setId(taskExtendRank.getNew_id());
        newTaskExtend.setRank(taskExtendRank.getOld_rank());
        list.add(oldTaskExtend);
        list.add(newTaskExtend);
        try {
            Integer count = taskService.updateTaskExtendRankByList(list);
            if(count > 0){
                return new TaskExtendResult(null, ResultType.SUCCESS, "任务详情顺序修改成功", null);
            }
            return new TaskExtendResult(null, ResultType.FAIL, "任务详情顺序修改失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(null, ResultType.ERROR, e.getMessage(), null);
        }
    }


    @PostMapping("/extend/del")
    public TaskExtendResult delTaskInfoById(@RequestBody List<String> list){
        if(null == list && list.size() == 0){
            return new TaskExtendResult(null, ResultType.FAIL, "任务ID列表不能为空", null);
        }
        try {
            Integer count = taskService.deleteTaskExtendByList(list);
            if(count > 0){
                return new TaskExtendResult(null, ResultType.SUCCESS, "任务详情删除成功", null);
            }
            return new TaskExtendResult(null, ResultType.FAIL, "任务详情删除失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(null, ResultType.ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/extend/add")
    public TaskExtendResult addTaskInfo(@RequestBody TaskExtendParams params){
        if(StringUtils.isBlank(params.getTask_id())){
            return new TaskExtendResult(null, ResultType.FAIL, "task_id不能为空", null);
        }
        if(null == params.getList()){
            return new TaskExtendResult(null, ResultType.FAIL, "步骤信息不能为空", null);
        }
        Integer count = 0;
        for (ApiDTO apiDTO: params.getList()) {
            try {
                TaskExtendDTO tt = new TaskExtendDTO();
                tt.setTask_id(params.getTask_id());
                tt.setProject_id(apiDTO.getProject_id());
                tt.setCase_id(apiDTO.getCase_id());
                tt.setApi_id(apiDTO.getId());
                tt.setApi_name(apiDTO.getName());
                TaskExtendDTO ts = taskService.getTaskExtendById(params.getTask_id());
                if(null != ts){
                    tt.setRank(ts.getRank() + 1);
                }else {
                    tt.setRank(1);
                }
                tt.setStatus("1");
                Integer cc = taskService.addTaskExtend(tt);
                if(cc > 0){
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        if(count > 0){
            return new TaskExtendResult(null, ResultType.SUCCESS, "添加步骤信息成功", null);
        }
        return new TaskExtendResult(null, ResultType.FAIL, "添加步骤信息失败", null);
    }

}
