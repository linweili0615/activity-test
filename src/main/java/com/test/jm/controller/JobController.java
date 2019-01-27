package com.test.jm.controller;

import com.test.jm.domain.Result;
import com.test.jm.domain.TaskJobInfo;
import com.test.jm.dto.TaskDTO;
import com.test.jm.dto.TaskJob;
import com.test.jm.keys.ResultType;
import com.test.jm.service.TaskJobService;
import com.test.jm.service.TaskService;
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

    @Autowired
    private TaskService taskService;

    @PostMapping("/job/add")
    public Result stopjob(@RequestBody TaskDTO taskDTO){
        if(taskDTO != null){
            if(taskJobService.create_job(taskDTO)){
                return new Result(ResultType.SUCCESS,"定时任务策略添加成功");
            }
            return new Result(ResultType.FAIL,"定时任务策略添加失败");
        }
        log.info("定时任务信息不能为空:{}");
        return new Result(ResultType.FAIL,"定时任务信息不能为空");
    }

    @PostMapping("/job/stop")
    public Result stopjob(@RequestBody String taskid){
        if(StringUtils.isBlank(taskid)){
            return new Result(ResultType.FAIL,"任务ID不能为空");
        }
        if(taskJobService.isStandBy()){
            log.info("调度器状态为standy");
            return new Result(ResultType.FAIL,"请先启动调度器");
        }
        if(!taskJobService.isJobExists(taskid)){
            return new Result(ResultType.FAIL,"请先启用任务");
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
        if(taskJobService.isStandBy()){
            return new Result(ResultType.FAIL,"请先启动调度器");
        }
        if(!taskJobService.isJobExists(taskid)){
            return new Result(ResultType.FAIL,"请先启用任务");
        }
        if(taskJobService.resume_job(taskid)){
            log.info("任务已启动:{}",taskid);
            return new Result(ResultType.SUCCESS,"任务已启动");
        }
        log.info("任务启动失败:{}",taskid);
        return new Result(ResultType.FAIL,"任务启动失败");
    }

    @GetMapping("/job/stop_all_jobs")
    public Result stopalljobs(){
        if(taskJobService.stopAllJobs()){
            return new Result(ResultType.SUCCESS,"调度器已暂停");
        }
        return new Result(ResultType.FAIL,"调度器暂停失败");
    }
    @GetMapping("/job/start_all_jobs")
    public Result startalljobs(){
        if(taskJobService.startAllJobs()){
            return new Result(ResultType.SUCCESS,"调度器已启动");
        }
        return new Result(ResultType.FAIL,"调度器启动失败");
    }

}
