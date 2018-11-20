package com.test.jm.controller;

import com.test.jm.domain.Result;
import com.test.jm.dto.test.CaseDTO;
import com.test.jm.service.CaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/case")
@RestController
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("/list")
    public Result getCaseList(@RequestBody String id){
        Result result = new Result();



        return result;
    }

    @PostMapping("/add")
    public Result addCase(@RequestBody CaseDTO caseDTO){
        Result result = new Result();
        if(StringUtils.isNotBlank(caseDTO.getProject_id()) && StringUtils.isNotBlank(caseDTO.getName())){
            try {
                String uid = caseService.addCase(caseDTO);
                result.setId(uid);
                result.setStatus("success");
                result.setMsg("添加测试集成功");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.setStatus("fail");
                result.setMsg("添加测试集失败");
                return result;
            }
        }
        result.setStatus("fail");
        result.setMsg("项目id或测试集名称不能为空");
        return result;
    }


}
