package com.test.jm.service;

import com.test.jm.dao.TaskResultDao;
import com.test.jm.dto.TaskResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskResultService {
    @Autowired
    private TaskResultDao taskResultDao;

    Integer addTaskResult(TaskResultDTO taskResultDTO){
        TaskResultDTO result = taskResultDao.getTaskResult(taskResultDTO);
        if(null != result){
            return taskResultDao.updateTaskResult(taskResultDTO);
        }
        return taskResultDao.addTaskResult(taskResultDTO);
    }

    List<TaskResultDTO> getTaskResultList(TaskResultDTO taskResultDTO){
        return taskResultDao.getTaskResultList(taskResultDTO);
    }

    TaskResultDTO getTaskResult(TaskResultDTO taskResultDTO){
        return taskResultDao.getTaskResult(taskResultDTO);
    }


}
