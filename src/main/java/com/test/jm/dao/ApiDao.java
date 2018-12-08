package com.test.jm.dao;

import com.test.jm.domain.ApiPage;
import com.test.jm.dto.test.ApiDTO;

import java.util.List;

public interface ApiDao {

    Integer addInterface(ApiDTO apiDTO);

    Integer editInterface(ApiDTO apiDTO);

    ApiDTO selectInterfaceById(String id);

    List<ApiDTO> getApiList(ApiPage apiPage);

    Integer deleteInterfaceById(String id);
}
