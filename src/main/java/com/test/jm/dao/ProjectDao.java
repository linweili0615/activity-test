package com.test.jm.dao;

import com.test.jm.dto.ProjectDTO;

import java.util.List;

public interface ProjectDao {

    List<ProjectDTO> getProjectList(ProjectDTO projectDTO);

    ProjectDTO selectProjectById(String id);

    Integer addProject(ProjectDTO projectDTO);

    Integer editProject(ProjectDTO projectDTO);

    Integer deleteProjectById(String id);


}
