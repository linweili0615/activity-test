package com.test.jm.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TaskTronJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(TaskTronJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("{} {}", LocalDateTime.now(),TaskTronJob.class.getName());
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        String taskid = String.valueOf(jobDataMap.get("taskid"));
        System.out.println("taskid: "+ taskid);
    }
}
