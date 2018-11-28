package com.test.jm.service;

import com.test.jm.dao.CaseDao;
import com.test.jm.dto.test.CaseDTO;
import com.test.jm.util.UserThreadLocal;
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
        caseDTO.setAuthor(UserThreadLocal.getUserInfo().getUser_name());
        Integer count = caseDao.addCase(caseDTO);
        if(count > 0 ){
            return id;
        }
        return null;
    }

    public Integer modifyCase(CaseDTO caseDTO){
        caseDTO.setUpdate_author(UserThreadLocal.getUserInfo().getUser_name());
        return caseDao.editCase(caseDTO);
    }


}
