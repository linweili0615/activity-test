package com.test.jm.service;

import com.test.jm.dao.TaskExtendDao;
import com.test.jm.domain.TaskExtendStatusParams;
import com.test.jm.dto.TaskExtendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TaskService {

    @Autowired
    private TaskExtendDao taskExtendDao;

    public List<TaskExtendDTO> getTaskExtendListById(TaskExtendDTO taskExtendDTO){
        return taskExtendDao.getTaskExtendListById(taskExtendDTO);
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

    public Integer updateTaskExtendStatusList(TaskExtendStatusParams params){
        return taskExtendDao.updateTaskExtendStatusList(params);
    }

    public Integer deleteTaskExtendByList(List<String> list){
        return taskExtendDao.deleteTaskExtendByList(list);
    }

    public Integer updateTaskExtendRankByList(List<TaskExtendDTO> list){
        return taskExtendDao.updateTaskExtendRankByList(list);
    }

}
