package com.test.jm.service;

import com.test.jm.dao.ProjectDao;
import com.test.jm.dto.test.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    public List<ProjectDTO> getProjectList(){
        return projectDao.getProjectList();
    }

    public ProjectDTO selectProjectById(String id){
        return projectDao.selectProjectById(id);
    }

    public Integer addProject(ProjectDTO projectDTO){
        return projectDao.addProject(projectDTO);
    }

    public Integer editProject(ProjectDTO projectDTO){
        return projectDao.editProject(projectDTO);
    }

    public Integer deleteProjectById(String id){
        return projectDao.deleteProjectById(id);
    }


}
