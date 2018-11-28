package com.test.jm.controller;

import com.test.jm.domain.CaseResult;
import com.test.jm.domain.Result;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.CaseDTO;
import com.test.jm.service.CaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/case")
@RestController
public class CaseController {

    @Autowired
    private CaseService caseService;

    @GetMapping("/list")
    public CaseResult getCaseList(){
        String id = "2616319c-b48a-404e-b82e-2c683eea6b0c";
        if(StringUtils.isBlank(id)){
            return new CaseResult("fail", "项目id不能为空", null);
        }

        try {
            List<CaseExtend> caseExtends = caseService.getCaseList(id);
            if(null == caseExtends){
                return new CaseResult("fail", "项目ID不存在", null);
            }
            return new CaseResult("success", "获取测试集成功", caseExtends);
        } catch (Exception e) {
            e.printStackTrace();
            return new CaseResult("fail", "获取测试集异常", null);
        }
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
