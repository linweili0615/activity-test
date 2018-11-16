package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.jm.domain.Page;
import com.test.jm.domain.ProjectResult;
import com.test.jm.domain.Result;
import com.test.jm.dto.test.ProjectDTO;
import com.test.jm.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            PageHelper.startPage(pageNo,pageSize);
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(page.getId());
            projectDTO.setProject_name(page.getProject_name());
            List<ProjectDTO> projectDTOList = projectService.getProjectList(projectDTO);
            PageInfo<ProjectDTO> pageInfo = new PageInfo<>(projectDTOList);
            int row_count = (int) pageInfo.getTotal();
            int pageCount = row_count % pageSize==0 ? row_count/pageSize : row_count/pageSize + 1;
            result.setProjectDTOList(pageInfo.getList());
            result.setTotal(pageInfo.getTotal());
            result.setPageSize(pageSize);
            result.setPageCount(pageCount);
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

    @PostMapping("/add")
    public Result addProject(@RequestBody ProjectDTO projectDTO){
        Result result = new Result();
        if(StringUtils.isNotBlank(projectDTO.getProject_name())){
            String p_id = projectService.addProject(projectDTO.getProject_name());
            if(StringUtils.isNotBlank(p_id)){
                result.setId(p_id);
                result.setStatus("success");
                result.setMsg("添加项目成功");
                return result;
            }
            result.setStatus("fail");
            result.setStatus("添加项目失败");
            return result;
        }
        result.setStatus("fail");
        result.setStatus("项目名称不能为空");
        return result;
    }
}
