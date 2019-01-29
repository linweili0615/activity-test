package com.test.jm.service;

import com.test.jm.domain.TaskJobInfo;
import com.test.jm.dto.TaskDTO;
import com.test.jm.jobs.TaskTronJob;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TaskJobService {

    private static final Logger log = LoggerFactory.getLogger(TaskJobService.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskService taskService;

    public boolean create_job(TaskDTO taskDTO) {
        if(StringUtils.isBlank(taskDTO.getId())){
            log.error("任务id不能为空",taskDTO.toString());
            return false;
        }
        if(isJobExists(taskDTO.getId())){
            return true;
        }
        TaskDTO tt = taskService.getTaskById(taskDTO.getId());
        if(null == tt){
            log.info("任务信息:{}不存在",taskDTO.getId());
            return false;
        }
        try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dateFormat.parse(tt.getEnd_time()).before(new Date())){
            log.info("任务已过期，不需要创建");
            return true;
        }
        if(dateFormat.parse(tt.getStart_time()).after(new Date())){
            log.info("未到任务开始时间，不需要创建");
            return true;
        }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("taskid",tt.getId());
            JobDetail jobDetail = JobBuilder.newJob(TaskTronJob.class)
                    .withIdentity(tt.getId())
                    .usingJobData(jobDataMap)
                    .build();

            Date start_time = dateFormat.parse(tt.getStart_time());
            Date end_time = dateFormat.parse(tt.getEnd_time());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(tt.getId())
                    .withSchedule(CronScheduleBuilder.cronSchedule(tt.getCron_expression()))
                    .startAt(start_time)
                    .endAt(end_time)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务添加成功：{}",tt.getId());
            resume_job(tt.getId());

            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("添加任务失败：{}",taskDTO.getId());
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("任务执行时间转换失败：start_time:{},end_time:{}",taskDTO.getStart_time(),taskDTO.getEnd_time());
            return false;
        }
    }

    public boolean pause_job(String job_key){
        if(StringUtils.isBlank(job_key)){
            log.info("任务id不能为空");
            return false;
        }
        try {
            scheduler.pauseJob(JobKey.jobKey(job_key));
            log.info("任务暂停成功：{}",job_key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("任务暂停失败：{}",job_key);
            return false;
        }
    }

    public boolean resume_job(String job_key){
        if(StringUtils.isBlank(job_key)){
            log.info("任务id不能为空");
            return false;
        }
        try {
            scheduler.resumeJob(JobKey.jobKey(job_key));
            log.info("任务已启动：{}",job_key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("任务启动失败：{}",job_key);
            return false;
        }
    }

    public boolean delete_job(String job_key){
        if(StringUtils.isBlank(job_key)){
            log.info("任务id不能为空");
            return false;
        }
        if(!isJobExists(job_key)){
            return true;
        }
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(job_key));
            log.info("trigger stop: {}",job_key);
            scheduler.unscheduleJob(TriggerKey.triggerKey(job_key));
            log.info("job unschedule: {}",job_key);
            scheduler.deleteJob(JobKey.jobKey(job_key));
            log.info("job delete: {}",job_key);
            log.info("任务删除成功! {}",job_key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("任务删除失败,{}",job_key);
            return false;
        }
    }

    public boolean update_job(TaskDTO taskDTO){
        if(StringUtils.isBlank(taskDTO.getId())){
            log.info("任务id不能为空");
            return false;
        }
        if(taskDTO.getStatus().equals("-1")){
            log.info("任务:{} 状态为-1，无需更新",taskDTO.getId());
            return false;
        }
        try {
            log.info("开始任务信息更新..");
            delete_job(taskDTO.getId());
            create_job(taskDTO);
            log.info("任务信息更新成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("任务信息更新失败");
            return false;
        }
    }

    public boolean isJobExists(String job_id){
        try {
            if(scheduler.checkExists(JobKey.jobKey(job_id))){
                log.info("job: {} 已存在",job_id);
                return true;
            }
            log.info("job: {} 不存在",job_id);
            return false;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("获取job是否存在失败,{}",job_id);
            return false;
        }

    }

    public boolean isStandBy(){
        log.info("开始获取调度器状态...");
        try {
            if(scheduler.isInStandbyMode()){
                log.info("调度器状态为: started");
                return true;
            }
            log.info("调度器状态为：standy");
            return false;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("获取调度器状态失败：{}",e.getMessage());
            return false;
        }
    }

    public boolean stopAllJobs(){
        log.info("开始暂停调度器...");
        try {
            scheduler.standby();
            log.info("调度器已暂停");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("调度器暂停异常：{}",e.getMessage());
            return false;
        }
    }

    public boolean startAllJobs(){
        log.info("开始启动调度器...");
        try {
            scheduler.start();
            log.info("调度器已启动...");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("调度器启动异常：{}",e.getMessage());
            return false;
        }
    }



}
