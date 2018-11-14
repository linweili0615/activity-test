package com.test.jm.controller;

import com.test.jm.domain.ProjectResult;
import com.test.jm.dto.test.ProjectDTO;
import com.test.jm.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/project")
@RestController
public class ProjectController {

    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/list")
    public ProjectResult getProjectList(){
        ProjectResult result = new ProjectResult();
        List<ProjectDTO> projectDTOList = null;
        try {
            projectDTOList = projectService.getProjectList();
            logger.info("getProjectList:{}",projectDTOList.toString());
            result.setProjectDTOList(projectDTOList);
            result.setTotal(projectDTOList.size());
            result.setStatus("success");
            result.setMsg("获取项目列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取getProjectList失败");
                result.setStatus("fail");
            result.setMsg("获取项目列表失败");
        }
        return result;
    }
}
