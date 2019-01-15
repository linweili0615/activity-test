package com.test.jm.service;

import com.test.jm.config.DataSource;
import com.test.jm.dao.ProjectDao;
import com.test.jm.dto.ProjectDTO;
import com.test.jm.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @DataSource("master")
    public List<ProjectDTO> getProjectList(ProjectDTO projectDTO){
        return projectDao.getProjectList(projectDTO);
    }

    public ProjectDTO selectProjectById(String id){
        return projectDao.selectProjectById(id);
    }

    public String addProject(String project_name){
        String uid = UUID.randomUUID().toString();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProject_name(project_name);
        projectDTO.setStatus(1);
        projectDTO.setAuthor(UserThreadLocal.getUserInfo().getUser_name());
        projectDTO.setId(uid);
        int count = projectDao.addProject(projectDTO);
        return uid;

    }

    public Integer editProject(ProjectDTO projectDTO){
        projectDTO.setUpdate_author(UserThreadLocal.getUserInfo().getUser_name());
        return projectDao.editProject(projectDTO);
    }

    public Integer deleteProjectById(String id){
            return projectDao.deleteProjectById(id);
    }


}
