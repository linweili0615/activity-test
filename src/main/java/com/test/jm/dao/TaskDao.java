package com.test.jm.dao;

import com.test.jm.domain.page.TaskPage;
import com.test.jm.dto.TaskDTO;
import com.test.jm.dto.TaskJob;

import java.util.List;

public interface TaskDao {
    Integer addTask(TaskDTO taskDTO);
    Integer updateTask(TaskDTO taskDTO);
    List<TaskJob> getTaskList(TaskPage taskPage);
    Integer deleteTask(String id);
    TaskDTO getTaskById(String id);
}
