package com.test.jm.dao;

import com.test.jm.dto.UserInfoDTO;

public interface UserInfoDao {

    public int addUserInfo(UserInfoDTO userInfoDTO);

    public UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO);

    public int updateUserInfo(UserInfoDTO userInfoDTO);

}
