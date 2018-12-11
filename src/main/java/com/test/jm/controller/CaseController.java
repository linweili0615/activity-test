package com.test.jm.controller;

import com.test.jm.domain.CaseResult;
import com.test.jm.domain.Result;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.CaseDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.CaseService;
import com.test.jm.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
                return new CaseResult("fail", "项目ID不存在", null);
            }
            if(StringUtils.isBlank(id)){
                return new CaseResult("success", "获取分组成功", CommonUtils.apiTree(caseExtends));
            }
            return new CaseResult("success", "获取分组成功", CommonUtils.caseTree(caseExtends));
        } catch (Exception e) {
            e.printStackTrace();
            return new CaseResult("fail", "获取分组异常", null);
        }
    }

    @PostMapping("/add")
    public Result addCase(@RequestBody CaseDTO caseDTO){
        Result result = new Result();
        if(StringUtils.isNotBlank(caseDTO.getProject_id()) && StringUtils.isNotBlank(caseDTO.getName())){
            try {
                String uid = caseService.addCase(caseDTO);
                result.setId(uid);
                result.setStatus(ResultType.SUCCESS);
                result.setMsg("分组已添加");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.setStatus(ResultType.ERROR);
                result.setMsg(e.getMessage());
                return result;
            }
        }
        result.setStatus(ResultType.ERROR);
        result.setMsg("项目id或分组名称不能为空");
        return result;
    }

    @PostMapping("/edit")
    public Result editCase(@RequestBody CaseDTO caseDTO){
        Result result = new Result();

            try {
                if(StringUtils.isNotBlank(caseDTO.getAuthor()) && caseDTO.getAuthor().equals("1") ){
                    Integer count = caseService.delCaseById(caseDTO);
                    if(count > 0){
                        result.setId(caseDTO.getId());
                        result.setStatus(ResultType.SUCCESS);
                        result.setMsg("分组已删除");
                        return result;
                    }else {
                        result.setId(caseDTO.getId());
                        result.setStatus(ResultType.SUCCESS);
                        result.setMsg("该分组不存在");
                        return result;
                    }

                }else {
                    if(StringUtils.isNotBlank(caseDTO.getProject_id()) && StringUtils.isNotBlank(caseDTO.getName())){
                        Integer count = caseService.modifyCase(caseDTO);
                        result.setId(caseDTO.getId());
                        if(count > 0){
                            result.setStatus(ResultType.SUCCESS);
                            result.setMsg("分组已修改");
                            return result;
                        }else {
                            result.setStatus(ResultType.FAIL);
                            result.setMsg("该分组不存在");
                            return result;
                        }
                    }
                    result.setStatus(ResultType.ERROR);
                    result.setMsg("项目id或测试集名称不能为空");
                    return result;

                }

            } catch (Exception e) {
                e.printStackTrace();
                result.setStatus(ResultType.ERROR);
                result.setMsg(e.getMessage());
                return result;
            }

    }


}
