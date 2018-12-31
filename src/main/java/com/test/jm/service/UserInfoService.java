package com.test.jm.service;

import com.test.jm.config.DataSource;
import com.test.jm.dao.UserInfoDao;
import com.test.jm.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @DataSource("master")
    public Integer addUserInfo(UserInfoDTO userInfoDTO){
        return userInfoDao.addUserInfo(userInfoDTO);
    }

    @DataSource("master")
    public UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO){
        return userInfoDao.getUserInfo(userInfoDTO);
    }

    @DataSource("master")
    public Integer updateUserInfo(UserInfoDTO userInfoDTO){
        return userInfoDao.updateUserInfo(userInfoDTO);
    }
}
