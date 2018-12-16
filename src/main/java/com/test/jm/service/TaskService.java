package com.test.jm.service;

import com.test.jm.dao.TaskExtendDao;
import com.test.jm.dto.test.TaskExtendDTO;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskExtendDao taskExtendDao;

    public List<TaskExtendDTO> getTaskExtendListById(String id){
        return taskExtendDao.getTaskExtendListById(id);
    }

    public Integer delTaskExtendById(String id){
        return taskExtendDao.removeTaskExtendById(id);
    }

    public Integer addTaskExtend(TaskExtendDTO taskExtendDTO){
        return taskExtendDao.addTaskExtend(taskExtendDTO);
    }

    public TaskExtendDTO getTaskExtendById(String id){
        return taskExtendDao.getTaskExtendById(id);
    }
}
