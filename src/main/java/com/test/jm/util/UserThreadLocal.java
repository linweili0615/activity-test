package com.test.jm.util;

import com.test.jm.dto.TokenDTO;

public class UserThreadLocal {

    private static ThreadLocal<TokenDTO> userInfo = new ThreadLocal<>();

    public static TokenDTO getUserInfo() {
        return userInfo.get();
    }

    public static void setUserInfo(TokenDTO tokenDTO) {
        userInfo.set(tokenDTO);
    }
}
