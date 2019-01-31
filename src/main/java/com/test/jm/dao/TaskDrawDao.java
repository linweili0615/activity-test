package com.test.jm.dao;

import com.test.jm.dto.TaskDrawDTO;

import java.util.List;

public interface TaskDrawDao {
    List<TaskDrawDTO> getTaskDraw(Integer id);
}
