package com.test.jm.dao;

import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.CaseDTO;

import java.util.List;

public interface CaseDao {
    Integer addCase(CaseDTO caseDTO);
    Integer delCase(CaseDTO caseDTO);
    Integer editCase(CaseDTO caseDTO);
    List<CaseExtend> selCaseByProjectId(String id);
//    List<CaseDTO> getCaseList();

}
