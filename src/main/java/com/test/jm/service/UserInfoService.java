package com.test.jm.service;

import com.test.jm.dao.UserInfoDao;
import com.test.jm.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    public int addUserInfo(UserInfoDTO userInfoDTO){
        return userInfoDao.addUserInfo(userInfoDTO);
    }

    public UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO){
        return userInfoDao.getUserInfo(userInfoDTO);
    }

//    public int updateUserInfo(UserInfoDTO userInfoDTO){
//
//    }
}
