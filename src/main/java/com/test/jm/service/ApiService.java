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
        try {
            apiDao.addInterface(apiDTO);
            return uid;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    };

    public Integer delInterfaceById(String id) {
        try {
            Integer res = apiDao.deleteInterfaceById(id);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public Integer editInterface(ApiDTO info) {
        try {
            Integer res = apiDao.editInterface(info);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer selectInterfaceById(String id) {
        try {
            Integer res = apiDao.selectInterfaceById(id);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

}
