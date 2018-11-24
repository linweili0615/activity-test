package com.test.jm.dao;

import com.test.jm.dto.test.ApiDTO;

public interface ApiDao {

    Integer addInterface(ApiDTO apiDTO);

    Integer editInterface(ApiDTO apiDTO);

    ApiDTO selectInterfaceById(String id);

    Integer deleteInterfaceById(String id);
}
