package com.test.jm.service;

import com.test.jm.dao.ApiDao;
import com.test.jm.dto.test.ApiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApiService {

    @Autowired
    private ApiDao apiDao;

    public String addInterface(ApiDTO apiDTO){
        String uid = UUID.randomUUID().toString();
        apiDTO.setId(uid);
        apiDTO.setAuthor("linweili");
        apiDao.addInterface(apiDTO);
        return uid;

    };

    public Integer delInterfaceById(String id) {
        return apiDao.deleteInterfaceById(id);

    }

    public Integer editInterface(ApiDTO info) {
      return apiDao.editInterface(info);
    }

    public ApiDTO selectInterfaceById(String id) {
        return apiDao.selectInterfaceById(id);

    }

}
