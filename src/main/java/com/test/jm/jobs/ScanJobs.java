package com.test.jm.jobs;
import com.test.jm.domain.page.TaskPage;
import com.test.jm.dto.TaskDTO;
import com.test.jm.dto.TaskJob;
import com.test.jm.service.TaskJobService;
import com.test.jm.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScanJobs {
    private static final Logger log = LoggerFactory.getLogger(ScanJobs.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskJobService taskJobService;

    //每5分钟
    @Scheduled(cron = "0 0/5 * * * ?")
    public void add_jobs() throws ParseException {
        log.info("开始获取任务列表...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TaskPage taskPage = new TaskPage();
        taskPage.setName("%%");
        List<TaskJob> jobList = taskService.getTaskList(taskPage);
        if(null != jobList && jobList.size() > 0){
            for (TaskJob taskJob:jobList) {
                Date start_time = dateFormat.parse(taskJob.getStart_time());
                Date end_time = dateFormat.parse(taskJob.getEnd_time());
                if(start_time.after(new Date())){
                    log.info("任务未开始，无需处理:{}",taskJob.getId());
                    continue;
                }
                if(end_time.before(new Date())){
                    log.info("任务已过期，开始删除:{}",taskJob.getId());
                    taskJobService.delete_job(taskJob.getId());
                    continue;
                }
                if(taskJob.getStatus().equals("-1")){
                    log.info("任务未启用，无需处理:{}",taskJob.getId());
                    continue;
                }

                TaskDTO taskDTO = new TaskDTO();
                taskDTO.setId(taskJob.getId());
                taskDTO.setStart_time(taskJob.getStart_time());
                taskDTO.setEnd_time(taskJob.getEnd_time());
                taskDTO.setCron_expression(taskJob.getCron_expression());
                log.info("获取到有效任务：{}，开始处理",taskDTO.getId());
                taskJobService.create_job(taskDTO);
            }
        }else {
            log.info("获取任务列表无记录，不需要处理");
        }
    }

}
