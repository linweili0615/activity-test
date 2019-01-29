package com.test.jm.service;

import com.test.jm.dao.TaskDao;
import com.test.jm.dao.TaskExtendDao;
import com.test.jm.domain.TaskExtendStatusParams;
import com.test.jm.domain.page.TaskPage;
import com.test.jm.dto.TaskDTO;
import com.test.jm.dto.TaskExtendDTO;
import com.test.jm.dto.TaskJob;
import com.test.jm.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TaskService {

    @Autowired
    private TaskExtendDao taskExtendDao;

    @Autowired
    private TaskDao taskDao;

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

    public Integer updateTask(TaskDTO taskDTO){
        taskDTO.setUpdate_author(UserThreadLocal.getUserInfo().getUser_name());
        return taskDao.updateTask(taskDTO);
    }

    public Integer addTask(TaskDTO taskDTO){
        taskDTO.setAuthor(UserThreadLocal.getUserInfo().getUser_name());
        taskDTO.setUpdate_author(UserThreadLocal.getUserInfo().getUser_name());
        taskDTO.setStatus("1");
        return taskDao.addTask(taskDTO);
    }

    public List<TaskJob> getTaskList(TaskPage taskPage){
        return taskDao.getTaskList(taskPage);
    }

    public Integer updateTaskStatus(TaskDTO task) {
        task.setUpdate_author(UserThreadLocal.getUserInfo().getUser_name());
        return taskDao.updateTask(task);
    }
    public Integer delTaskById(String id) {
        return taskDao.deleteTask(id);
    }

    public Integer delTaskExtendByTaskId(String task_id) {
        return taskExtendDao.deleteTaskExtendByTakk(task_id);
    }

    public TaskDTO getTaskById(String id) {
        return taskDao.getTaskById(id);
    }
}
