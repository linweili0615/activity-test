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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

    @RequestMapping("/test")
    public TaskExtendResult runCase(@RequestBody String task_id){
        if(StringUtils.isBlank(task_id)){
            return new TaskExtendResult(ResultType.FAIL, "task_id不能为空");
        }
        long time = System.currentTimeMillis() + TIMEOUT;
        String key = task_id + UserThreadLocal.getUserInfo().getUser_id();
        if(!redisUtil.lock(key, String.valueOf(time))){
            return new TaskExtendResult(ResultType.FAIL, "操作太频繁了哈");
        }
        try {
            File file1 = new File(new File("").getAbsolutePath()+"/log/"+task_id+".log");
            if(file1.exists()){
                FileOutputStream out = new FileOutputStream(new File("").getAbsolutePath()+"/log/"+task_id+ UserThreadLocal.getUserInfo().getUser_id()+".log");
                out.write(new String("").getBytes());
                out.close();
            }
            List<HttpClientResult> list = requestService.runCase(task_id);
            if(null != list){
                return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行成功", list);
            }
            return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(task_id, ResultType.ERROR, "task执行异常", null);
        }finally {
            redisUtil.unlock(key, String.valueOf(time));
        }
    }

    @PostMapping("/extend/info")
    public TaskExtendResult getTaskInfo(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new TaskExtendResult(null, ResultType.FAIL, "任务ID不能为空", null);
        }
        try {
            TaskExtendDTO taskExtendDTO = new TaskExtendDTO();
            taskExtendDTO.setTask_id(id);
            List<TaskExtendDTO> data = taskService.getTaskExtendListById(taskExtendDTO);
            if(null == data){
                return new TaskExtendResult(id, ResultType.FAIL, "任务详情无记录", data);
            }
            return new TaskExtendResult(id, ResultType.SUCCESS, "获取任务详情成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(id, ResultType.ERROR, e.getMessage(), null);
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
