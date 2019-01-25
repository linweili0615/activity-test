package com.test.jm.dao;

import com.test.jm.dto.TaskDTO;

import java.util.List;

public interface TaskDao {
    Integer updateTask(TaskDTO taskDTO);
    List<TaskDTO> getTaskList();
}
