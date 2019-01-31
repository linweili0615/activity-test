package com.test.jm.service;

import com.test.jm.dao.TaskCaseLogDao;
import com.test.jm.dto.TaskCaseLog;
import com.test.jm.dto.TaskResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCaseLogService {
    @Autowired
    private TaskCaseLogDao taskCaseLogDao;

    TaskCaseLog getTaskCaseLog(TaskCaseLog taskCaseLog){
        return taskCaseLogDao.getTaskCaseLogById(taskCaseLog);
    }

    Integer insertTaskCaseLog(TaskCaseLog taskCaseLog){
        return taskCaseLogDao.addTaskCaseLog(taskCaseLog);
    }


}
