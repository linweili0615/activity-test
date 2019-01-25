package com.test.jm.controller;

import com.test.jm.domain.Result;
import com.test.jm.domain.TaskJobInfo;
import com.test.jm.dto.TaskDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.TaskJobService;
import com.test.jm.service.TaskService;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class JobController {

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/job/add")
    public Result testjob(TaskDTO task){
        if(StringUtils.isBlank(task.getStart_time()) || StringUtils.isBlank(task.getEnd_time())){
            return new Result(ResultType.FAIL,"任务执行时间段不能为空");
        }
        if(StringUtils.isBlank(task.getCron_expression())){
            return new Result(ResultType.FAIL,"任务定时策略不能为空");
        }
        if(!CronExpression.isValidExpression(task.getCron_expression())){
            return new Result(ResultType.FAIL,"请输入正确的定时策略");
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(UUID.randomUUID().toString());
        taskDTO.setName(task.getName());
        taskDTO.setStart_time(task.getStart_time());
        taskDTO.setEnd_time(task.getEnd_time());
        taskDTO.setCron_expression(task.getCron_expression());
        try {
            Integer count = taskService.addTask(taskDTO);
            if(count > 0){
                if(taskJobService.create_job(task)){
                    return new Result(ResultType.SUCCESS,"添加定时任务成功",taskDTO.getId());
                }else {
                    return new Result(ResultType.FAIL,"添加定时任务失败",taskDTO.getId());
                }
            }
            return new Result(ResultType.FAIL,"添加任务失败",taskDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,"添加定时任务成功，更新任务信息失败",taskDTO.getId());
        }
    }

    @RequestMapping("/job/stop")
    public String stopjob(){
        String taskid = "123698568f74sdf9874fds984dfs";
        taskJobService.pause_job(taskid);
        return "/job/stop";
    }

    @RequestMapping("/job/resume")
    public String resumejob(){
        String taskid = "123698568f74sdf9874fds984dfs";
        taskJobService.resume_job(taskid);
        return "/job/resume";
    }

    @RequestMapping("/job/delete")
    public String deletejob(){
        String taskid = "123698568f74sdf9874fds984dfs";
        taskJobService.delete_job(taskid);
        return "/job/delete";
    }

}
