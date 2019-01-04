package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.jm.domain.page.ProjectPage;
import com.test.jm.domain.ProjectResult;
import com.test.jm.domain.Result;
import com.test.jm.dto.test.ProjectDTO;
import com.test.jm.keys.ResultType;
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
    public ProjectResult getProjectList(@RequestBody ProjectPage projectPage){
        Integer pageSize = projectPage.getPageSize();
        Integer pageNo = projectPage.getPageNo();
        if(null == pageSize){
            pageSize = 15;
        }
        if(null == pageNo){
            pageNo = 1;
        }
        ProjectResult result = new ProjectResult();
        try {
            PageHelper.startPage(pageNo,pageSize);
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(projectPage.getId());
            projectDTO.setProject_name(projectPage.getProject_name());
            List<ProjectDTO> projectDTOList = projectService.getProjectList(projectDTO);
            PageInfo<ProjectDTO> pageInfo = new PageInfo<>(projectDTOList);
            int row_count = (int) pageInfo.getTotal();
            int pageCount = row_count % pageSize==0 ? row_count/pageSize : row_count/pageSize + 1;
            result.setProjectDTOList(pageInfo.getList());
            result.setTotal(pageInfo.getTotal());
            result.setPageSize(pageSize);
            result.setPageCount(pageCount);
            result.setPageNo(pageNo);
            result.setStatus(ResultType.SUCCESS);
            result.setMsg("获取项目列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取getProjectList失败");
            result.setStatus(ResultType.FAIL);
            result.setMsg("获取项目列表失败");
        }
        return result;
    }

    @PostMapping("/add")
    public Result addProject(@RequestBody String project_name){
        Result result = new Result();
        if(StringUtils.isNotBlank(project_name)){
            String p_id = projectService.addProject(project_name);
            if(StringUtils.isNotBlank(p_id)){
                result.setId(p_id);
                result.setStatus(ResultType.SUCCESS);
                result.setMsg("添加项目成功");
                return result;
            }
            result.setStatus(ResultType.FAIL);
            result.setStatus("添加项目失败");
            return result;
        }
        result.setStatus(ResultType.FAIL);
        result.setStatus("项目名称不能为空");
        return result;
    }

    @PostMapping("/del")
    public Result delProject(@RequestBody String id){
        Result result = new Result();
        if(StringUtils.isNotBlank(id)){
            Integer count = projectService.deleteProjectById(id);
            if(count > 0){
                result.setId(id);
                result.setStatus(ResultType.SUCCESS);
                result.setMsg("删除项目成功");
                return result;
            }
            result.setId(id);
            result.setStatus(ResultType.FAIL);
            result.setMsg("删除项目失败");
        }
        result.setStatus(ResultType.FAIL);
        result.setStatus("项目id不能为空");
        return result;
    }

    @PostMapping("/handle")
    public Result handleProject(@RequestBody ProjectDTO projectDTO){
        Result result = new Result();
        if(StringUtils.isNotBlank(projectDTO.getId()) && StringUtils.isNotBlank(projectDTO.getStatus().toString())){
            ProjectDTO projectDTO1 = new ProjectDTO();
            projectDTO1.setId(projectDTO.getId());
            projectDTO1.setStatus(projectDTO.getStatus() * -1);
            Integer count = projectService.editProject(projectDTO1);
            if(count > 0){
                result.setId(projectDTO1.getId());
                result.setStatus(ResultType.SUCCESS);
                result.setMsg("修改项目状态成功");
                return result;
            }
            result.setId(projectDTO.getId());
            result.setStatus(ResultType.FAIL);
            result.setMsg("修改项目状态失败");
        }
        result.setStatus(ResultType.FAIL);
        result.setStatus("参数不能为空");
        return result;
    }

    @PostMapping("/update")
    public Result updateProject(@RequestBody ProjectDTO projectDTO){
        Result result = new Result();
        if(StringUtils.isNotBlank(projectDTO.getId()) && StringUtils.isNotBlank(projectDTO.getStatus().toString())){
            Integer count = projectService.editProject(projectDTO);
            if(count > 0){
                result.setId(projectDTO.getId());
                result.setStatus(ResultType.SUCCESS);
                result.setMsg("修改项目成功");
                return result;
            }
            result.setId(projectDTO.getId());
            result.setStatus(ResultType.FAIL);
            result.setMsg("修改项目失败");
        }
        result.setStatus(ResultType.FAIL);
        result.setStatus("项目id不能为空");
        return result;
    }



}
