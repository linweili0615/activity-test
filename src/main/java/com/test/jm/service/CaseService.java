package com.test.jm.service;

import com.test.jm.dao.CaseDao;
import com.test.jm.dto.test.CaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CaseService {

    @Autowired
    private CaseDao caseDao;

    public String addCase(CaseDTO caseDTO){
        String id = UUID.randomUUID().toString();
        caseDTO.setId(id);
        Integer count = caseDao.addCase(caseDTO);
        if(count > 0 ){
            return id;
        }
        return null;
    }
}
