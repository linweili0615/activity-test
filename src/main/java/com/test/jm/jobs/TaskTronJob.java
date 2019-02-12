package com.test.jm.jobs;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.keys.TaskType;
import com.test.jm.service.RequestService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TaskTronJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(TaskTronJob.class);

    @Autowired
    private RequestService requestService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        String taskid = String.valueOf(jobDataMap.get("taskid"));
        try {
            requestService.runCase(TaskType.JOB, taskid);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("{}，运行异常{}",TaskTronJob.class.getName(),e.getMessage());
        }
    }
}
