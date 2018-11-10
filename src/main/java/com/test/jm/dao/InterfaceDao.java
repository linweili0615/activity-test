package com.test.jm.dao;

import com.test.jm.dto.test.InterfaceDTO;

public interface InterfaceDao {

    Integer addInterface(InterfaceDTO interfaceDTO);

    Integer editInterface(InterfaceDTO interfaceDTO);

    Integer selectInterfaceById(String id);

    Integer deleteInterfaceById(String id);
}
