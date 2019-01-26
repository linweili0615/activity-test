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

    public boolean create_job(TaskDTO taskDTO){
        if(StringUtils.isBlank(taskDTO.getId())){
            log.error("任务id不能为空",taskDTO.toString());
            return false;
        }
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("taskid",taskDTO.getId());
            JobDetail jobDetail = JobBuilder.newJob(TaskTronJob.class)
                    .withIdentity(taskDTO.getId())
                    .usingJobData(jobDataMap)
                    .build();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start_time = dateFormat.parse(taskDTO.getStart_time());
            Date end_time = dateFormat.parse(taskDTO.getEnd_time());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(taskDTO.getId())
                    .withSchedule(CronScheduleBuilder.cronSchedule(taskDTO.getCron_expression()))
                    .startAt(start_time)
                    .endAt(end_time)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务添加成功：{}",taskDTO.getId());
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
            log.info("任务恢复成功：{}",job_key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("任务恢复失败：{}",job_key);
            return false;
        }
    }

    public boolean delete_job(String job_key){
        if(StringUtils.isBlank(job_key)){
            log.info("任务id不能为空");
            return false;
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

    public boolean update_job(TaskJobInfo taskJobInfo){
        if(StringUtils.isBlank(taskJobInfo.getTask_id())){
            log.info("任务id不能为空");
            return false;
        }
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(taskJobInfo.getTask_id());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(taskJobInfo.getCron_expression());
            trigger.getTriggerBuilder().withIdentity(taskJobInfo.getTask_id()).withSchedule(scheduleBuilder);
            scheduler.rescheduleJob(triggerKey,trigger);
            log.info("任务信息更新成功");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("任务信息更新失败");
            return false;
        }
    }

    public boolean stopAllJobs(){
        log.info("开始关闭所有定时任务...");
        try {
            scheduler.standby();
            log.info("所有定时任务已关闭");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("定时任务关闭异常：{}",e.getMessage());
            return false;
        }
    }

    public boolean startAllJobs(){
        log.info("开始启动定时任务...");
        try {
            scheduler.start();
            log.info("定时任务已启动...");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("定时任务启动异常：{}",e.getMessage());
            return false;
        }
    }



}
