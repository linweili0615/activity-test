package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.jm.domain.Page;
import com.test.jm.domain.ProjectResult;
import com.test.jm.dto.test.ProjectDTO;
import com.test.jm.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/list")
    public ProjectResult getProjectList(@RequestBody Page page){
        Integer pageSize = page.getPageSize();
        Integer pageNo = page.getPageNo();
        if(null == pageSize){
            pageSize = 10;
        }
        if(null == pageNo){
            pageNo = 1;
        }
        ProjectResult result = new ProjectResult();
        try {

            List<ProjectDTO> projectDTOList = projectService.getProjectList();
            if(pageNo > projectDTOList.size()/pageSize){
                pageNo = projectDTOList.size()%pageSize==0 ? projectDTOList.size()/pageSize : projectDTOList.size()/pageSize + 1;
            }
            PageHelper.startPage(pageNo,pageSize);
            PageInfo<ProjectDTO> pageInfo = new PageInfo<>(projectDTOList);
            result.setProjectDTOList(pageInfo.getList());
            result.setTotal(pageInfo.getTotal());
            result.setPageSize(pageSize);
            result.setPageNo(pageNo);
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
