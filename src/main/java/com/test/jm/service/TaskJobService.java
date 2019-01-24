package com.test.jm.service;

import com.test.jm.domain.TaskJobInfo;
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

    public boolean create_job(TaskJobInfo taskJobInfo){
        if(StringUtils.isBlank(taskJobInfo.getTask_id())){
            log.error("任务id不能为空,{}",taskJobInfo.toString());
            return false;
        }
        log.info("1");
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("taskid",taskJobInfo.getTask_id());
            JobDetail jobDetail = JobBuilder.newJob(TaskTronJob.class)
                    .withIdentity(taskJobInfo.getTask_id())
                    .usingJobData(jobDataMap)
                    .build();
            log.info("2");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start_time = dateFormat.parse(taskJobInfo.getStart_time());
            Date end_time = dateFormat.parse(taskJobInfo.getEnd_time());
            log.info("3");
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(taskJobInfo.getTask_id())
                    .withSchedule(CronScheduleBuilder.cronSchedule(taskJobInfo.getCron_expression()))
                    .startNow()
                    .endAt(end_time)
                    .build();
            log.info("4");
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务添加成功：{}",taskJobInfo.getTask_id());
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("添加任务失败：{}",taskJobInfo.getTask_id());
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("任务执行时间转换失败：start_time:{},end_time:{}",taskJobInfo.getStart_time(),taskJobInfo.getEnd_time());
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



}
