package com.test.jm.dao;

import com.test.jm.dto.test.CaseDTO;

import java.util.List;

public interface CaseDao {
    Integer addCase(CaseDTO caseDTO);
    Integer delCase(String id);
    Integer editCase(CaseDTO caseDTO);
    CaseDTO selCaseById(String id);
    List<CaseDTO> getCaseList();

}
