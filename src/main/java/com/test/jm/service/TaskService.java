package com.test.jm.service;

import com.test.jm.dao.TaskExtendDao;
import com.test.jm.dto.test.TaskExtendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskExtendDao taskExtendDao;

    public List<TaskExtendDTO> getTaskById(String id){
        return taskExtendDao.getTaskById(id);
    }
}
