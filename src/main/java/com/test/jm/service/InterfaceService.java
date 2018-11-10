package com.test.jm.service;

import com.test.jm.dao.InterfaceDao;
import com.test.jm.dto.test.InterfaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InterfaceService {

    @Autowired
    private InterfaceDao interfaceDao;

    public String addInterface(InterfaceDTO interfaceDTO){
        String uid = UUID.randomUUID().toString();
        interfaceDTO.setId(uid);
        interfaceDTO.setAuthor("linweili");
        interfaceDao.addInterface(interfaceDTO);
        return uid;
    };

    public Integer editInterface(InterfaceDTO info) {
        try {
            Integer res = interfaceDao.editInterface(info);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer selectInterfaceById(String id) {
        try {
            Integer res = interfaceDao.selectInterfaceById(id);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
}
