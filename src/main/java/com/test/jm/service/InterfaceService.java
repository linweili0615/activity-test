package com.test.jm.service;

import com.test.jm.dto.test.InterfaceDTO;
import org.springframework.stereotype.Service;

@Service
public class InterfaceService {

    public String addInterface(InterfaceDTO interfaceDTO){
        interfaceDTO.setAuthor("");
        return null;
    };

}
