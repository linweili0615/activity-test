package com.test.jm.controller;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.domain.TaskExtendParams;
import com.test.jm.domain.TaskExtendResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.RequestService;
import com.test.jm.service.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RequestService requestService;

    @RequestMapping("/test")
    public TaskExtendResult runCase(@RequestBody String task_id){
        if(StringUtils.isBlank(task_id)){
            return new TaskExtendResult(ResultType.FAIL, "task_id不能为空");
        }
        try {
            List<HttpClientResult> list = requestService.runCase(task_id);
            if(null != list){
                return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行成功", list);
            }
            return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(task_id, ResultType.ERROR, "task执行异常", null);
        }
    }

    @PostMapping("/extend/info")
    public TaskExtendResult getTaskInfo(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new TaskExtendResult(null, ResultType.FAIL, "任务ID不能为空", null);
        }
        try {
            List<TaskExtendDTO> data = taskService.getTaskExtendListById(id);
            if(null == data){
                return new TaskExtendResult(id, ResultType.FAIL, "任务详情无记录", null);
            }
            return new TaskExtendResult(id, ResultType.SUCCESS, "获取任务详情成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(id, ResultType.ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/extend/del")
    public TaskExtendResult delTaskInfoById(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new TaskExtendResult(null, ResultType.FAIL, "接口ID不能为空", null);
        }
        try {
            Integer count = taskService.delTaskExtendById(id);
            if(count > 0){
                return new TaskExtendResult(id, ResultType.SUCCESS, "任务详情删除成功", null);
            }
            return new TaskExtendResult(id, ResultType.FAIL, "任务详情删除失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(id, ResultType.ERROR, e.getMessage(), null);
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
                tt.setApi_id(apiDTO.getId());
                tt.setApi_name(apiDTO.getName());
                TaskExtendDTO ts = taskService.getTaskExtendById(params.getTask_id());
                if(null != ts){
                    tt.setRank(ts.getRank() + 1);
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
