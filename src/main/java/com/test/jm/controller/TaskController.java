package com.test.jm.controller;

import com.test.jm.domain.TaskExtendResult;
import com.test.jm.dto.test.TaskExtendDTO;
import com.test.jm.keys.ResultType;
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

    @PostMapping("/extend/info")
    public TaskExtendResult getTaskInfo(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new TaskExtendResult(null, ResultType.FAIL, "任务ID不能为空", null);
        }
        try {
            List<TaskExtendDTO> data = taskService.getTaskExtendById(id);
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



}
