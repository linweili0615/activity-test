package com.test.jm.dao;

import com.test.jm.dto.test.TaskExtendDTO;

import java.util.List;

public interface TaskExtendDao {

    Integer addTaskExtend(TaskExtendDTO taskExtendDTO);

    Integer removeTaskExtendById(String id);

    Integer modifyTaskExtend(TaskExtendDTO taskExtendDTO);

    List<TaskExtendDTO> getTaskExtendById(String id);

    List<TaskExtendDTO> getTaskExtendList();

}