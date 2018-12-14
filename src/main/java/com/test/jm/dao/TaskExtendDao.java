package com.test.jm.dao;

import com.test.jm.dto.test.TaskExtendDTO;

import java.util.List;

public interface TaskExtendDao {

    Integer addTask(TaskExtendDTO taskExtendDTO);

    Integer removeTask(String id);

    Integer modifyTask(TaskExtendDTO taskExtendDTO);

    List<TaskExtendDTO> getTaskById(String id);

    List<TaskExtendDTO> getTaskList();

}
