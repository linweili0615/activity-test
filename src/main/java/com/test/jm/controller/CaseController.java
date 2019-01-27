package com.test.jm.controller;

import com.test.jm.domain.CaseResult;
import com.test.jm.domain.Result;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.CaseDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.CaseService;
import com.test.jm.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/case")
@RestController
public class CaseController {

    @Autowired
    private CaseService caseService;

    @RequestMapping("/list")
    public CaseResult getCaseList(@RequestBody(required = false) String id){
        try {
            List<CaseExtend> caseExtends = caseService.getCaseList(id);
            if(null == caseExtends){
                return new CaseResult(ResultType.NOT_FOUND, "项目ID不存在", null);
            }
            if(StringUtils.isBlank(id)){
                return new CaseResult(ResultType.SUCCESS, "获取分组成功", CommonUtils.apiTree(caseExtends));
            }
            return new CaseResult(ResultType.SUCCESS, "获取分组成功", CommonUtils.caseTree(caseExtends));
        } catch (Exception e) {
            e.printStackTrace();
            return new CaseResult(ResultType.ERROR, "获取分组异常");
        }
    }

    @PostMapping("/add")
    public Result addCase(@RequestBody CaseDTO caseDTO){
        if(StringUtils.isBlank(caseDTO.getProject_id()) && StringUtils.isBlank(caseDTO.getName())){
            return new Result(ResultType.FAIL,"分组信息不能为空");
        }
        try {
            caseService.addCase(caseDTO);
            return new Result(ResultType.SUCCESS,"分组已添加");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        }
    }

    @PostMapping("/edit")
    public Result editCase(@RequestBody CaseDTO caseDTO){
        if(StringUtils.isBlank(caseDTO.getProject_id()) && StringUtils.isBlank(caseDTO.getName())){
            return new Result(ResultType.FAIL,"项目ID或分组名称不能为空");
        }
        try {
            if(StringUtils.isNotBlank(caseDTO.getAuthor()) && caseDTO.getAuthor().equals("1")){
                Integer count = caseService.delCaseById(caseDTO);
                if(count > 0){
                    return new Result(ResultType.SUCCESS,"分组已删除");
                }else {
                    return new Result(ResultType.FAIL,"该分组不存在");
                }
            } else {
                    Integer count = caseService.modifyCase(caseDTO);
                    if(count > 0){
                        return new Result(ResultType.SUCCESS,"分组已修改");
                    }else {
                        return new Result(ResultType.FAIL,"该分组不存在");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        }
    }

}
