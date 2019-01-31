package com.test.jm.dao;

import com.test.jm.dto.TaskCaseLog;
import com.test.jm.dto.TaskResultDTO;

import java.util.List;

public interface TaskCaseLogDao {
    TaskCaseLog getTaskCaseLogById(TaskCaseLog taskCaseLog);

    Integer addTaskCaseLog(TaskCaseLog taskCaseLog);
}
