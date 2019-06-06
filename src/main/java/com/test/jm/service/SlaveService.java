package com.test.jm.service;

import com.test.jm.config.DataSource;
import com.test.jm.dao.SlaveDao;
import com.test.jm.dto.Person;
//import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlaveService {
    @Autowired
    private SlaveDao slaveDao;

    @DataSource("slave")
    public Person getPerson(){
        return slaveDao.getSlave();
    }
}
