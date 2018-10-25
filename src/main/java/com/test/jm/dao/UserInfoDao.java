package com.test.jm.dao;

import com.test.jm.dto.UserInfoDTO;

public interface UserInfoDao {

     Integer addUserInfo(UserInfoDTO userInfoDTO);

     UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO);

     Integer updateUserInfo(UserInfoDTO userInfoDTO);

}
