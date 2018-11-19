package com.test.jm.controller;

import com.test.jm.domain.CaseResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/case")
@RestController
public class CaseController {

    @PostMapping("/list")
    public CaseResult getCaseList(@RequestBody String id){
        CaseResult caseResult = new CaseResult();



        return caseResult;
    }


}
