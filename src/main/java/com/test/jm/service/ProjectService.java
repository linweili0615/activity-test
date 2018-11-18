package com.test.jm.service;

import com.test.jm.dao.ProjectDao;
import com.test.jm.dto.test.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

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
        projectDTO.setAuthor("linweili");
        projectDTO.setId(uid);
        try {
            int count = projectDao.addProject(projectDTO);
            if(count > 0){
                return uid;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Integer editProject(ProjectDTO projectDTO){
        Integer count = 0;
        try {
            count = projectDao.editProject(projectDTO);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        }
    }

    public Integer deleteProjectById(String id){
        Integer count = 0;
        try {
            count = projectDao.deleteProjectById(id);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        }
    }


}
