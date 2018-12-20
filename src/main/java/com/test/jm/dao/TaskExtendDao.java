package com.test.jm.dao;

import com.test.jm.domain.TaskExtendStatusParams;
import com.test.jm.dto.test.TaskExtendDTO;
import java.util.List;
import java.util.Map;

public interface TaskExtendDao {

    Integer addTaskExtend(TaskExtendDTO taskExtendDTO);

    Integer removeTaskExtendById(String id);

    Integer modifyTaskExtend(TaskExtendDTO taskExtendDTO);

    List<TaskExtendDTO> getTaskExtendListById(TaskExtendDTO taskExtendDTO);

    TaskExtendDTO getTaskExtendById(String id);

    List<TaskExtendDTO> getTaskExtendList();

    Integer updateTaskExtendStatusList(TaskExtendStatusParams params);

    Integer deleteTaskExtendByList(List<String> list);

    Integer updateTaskExtendRankByList(List<TaskExtendDTO> list);

}
