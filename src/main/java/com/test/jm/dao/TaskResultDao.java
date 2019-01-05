package com.test.jm.dao;

import com.test.jm.dto.TaskResultDTO;

import java.util.List;

public interface TaskResultDao {
    Integer addTaskResult(TaskResultDTO taskResultDTO);

    Integer updateTaskResult(TaskResultDTO taskResultDTO);

    List<TaskResultDTO> getTaskResultList(TaskResultDTO taskResultDTO);

    TaskResultDTO getTaskResult(TaskResultDTO taskResultDTO);
}
