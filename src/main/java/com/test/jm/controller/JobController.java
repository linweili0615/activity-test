package com.test.jm.controller;

import com.test.jm.domain.Result;
import com.test.jm.keys.ResultType;
import com.test.jm.service.TaskJobService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private TaskJobService taskJobService;

    @PostMapping("/job/stop")
    public Result stopjob(@RequestBody String taskid){
        if(StringUtils.isBlank(taskid)){
            return new Result(ResultType.FAIL,"任务ID不能为空");
        }
        if(taskJobService.pause_job(taskid)){
            log.info("任务已暂停:{}",taskid);
            return new Result(ResultType.SUCCESS,"任务已暂停");
        }
        log.info("任务暂停失败:{}",taskid);
        return new Result(ResultType.FAIL,"任务暂停失败");

    }

    @PostMapping("/job/resume")
    public Result resumejob(@RequestBody String taskid){
        if(StringUtils.isBlank(taskid)){
            return new Result(ResultType.FAIL,"任务ID不能为空");
        }
        if(taskJobService.resume_job(taskid)){
            log.info("任务已恢复:{}",taskid);
            return new Result(ResultType.SUCCESS,"任务已恢复");
        }
        log.info("任务恢复失败:{}",taskid);
        return new Result(ResultType.FAIL,"任务恢复失败");
    }

    @GetMapping("/job/stop_all_jobs")
    public Result stopalljobs(){
        if(taskJobService.stopAllJobs()){
            return new Result(ResultType.SUCCESS,"任务已暂停");
        }
        return new Result(ResultType.FAIL,"任务暂停失败");
    }
    @GetMapping("/job/start_all_jobs")
    public Result startalljobs(){
        if(taskJobService.startAllJobs()){
            return new Result(ResultType.SUCCESS,"任务已启动");
        }
        return new Result(ResultType.FAIL,"任务启动失败");
    }

}
