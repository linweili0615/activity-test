package com.test.jm.controller;

import com.test.jm.domain.TaskJobInfo;
import com.test.jm.service.TaskJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @Autowired
    private TaskJobService taskJobService;

    @RequestMapping("/job/add")
    public String testjob(){
        String taskid = "123698568f74sdf9874fds984dfs";
        TaskJobInfo taskJobInfo = new TaskJobInfo();
        taskJobInfo.setTask_id(taskid);
        taskJobInfo.setJob_name(taskid);
        taskJobInfo.setCron_expression("0 0/2 * * * ? ");
        taskJobInfo.setStart_time("2019-01-24 23:07:30");
        taskJobInfo.setEnd_time("2019-01-24 23:30:30");
        taskJobService.create_job(taskJobInfo);
        return "/job/add";
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
