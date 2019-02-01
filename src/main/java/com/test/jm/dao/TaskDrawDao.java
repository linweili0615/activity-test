package com.test.jm.dao;

import com.test.jm.dto.TaskDrawDTO;
import com.test.jm.dto.TaskExtendDTO;

import java.util.List;

public interface TaskDrawDao {
    List<TaskDrawDTO> getTaskDraw(Integer id);

    Integer createTaskDraw(TaskDrawDTO taskDrawDTO);
}
