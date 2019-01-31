package com.test.jm.service;

import com.test.jm.dao.TaskDrawDao;
import com.test.jm.dto.TaskDrawDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskDrawService {

    @Autowired
    private TaskDrawDao taskDrawDao;
    TaskDrawDTO getTaskDrawById(Integer id){
        return taskDrawDao.getTaskDraw(id);
    }
}
