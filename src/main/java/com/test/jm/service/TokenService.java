package com.test.jm.service;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.dto.UserInfoDTO;
import com.test.jm.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private TokenDao tokenDao;

    public TokenDTO findTokenByUserId(String userid){
        return tokenDao.findTokenByUserId(userid);
    }

    public Integer addToken(UserInfoDTO userInfoDTO){
        //过期时间
        long expirationdate = 30 ;
        String token = TokenUtils.createJwtToken(userInfoDTO.getId(), expirationdate);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(token);
        tokenDTO.setUser_id(userInfoDTO.getId());
        tokenDTO.setExpire_time(LocalDateTime.now().plusMinutes(expirationdate));
        return tokenDao.addToken(tokenDTO);
    }

}
