package com.test.jm.service;

import com.test.jm.config.DataSource;
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

    @DataSource("master")
    public TokenDTO findTokenByUserId(String userid){
        return tokenDao.findTokenByUserId(userid);
    }

    @DataSource("master")
    public Integer updateToken(String token){
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(token);
        tokenDTO.setStatus("1");
        return tokenDao.updateToken(tokenDTO);
    }


    @DataSource("master")
    public Integer addToken(TokenDTO tokenDTO, long expirationdate){
        if(findTokenByUserId(tokenDTO.getUser_id())!=null){
            System.out.println("token expire_time: " + LocalDateTime.now().plusHours(expirationdate));
            tokenDTO.setExpire_time(LocalDateTime.now().plusHours(expirationdate));
            return tokenDao.updateToken(tokenDTO);
        }
        tokenDTO.setStatus("0");
        System.out.println("token expire_time: " + LocalDateTime.now().plusHours(expirationdate));
        tokenDTO.setExpire_time(LocalDateTime.now().plusHours(expirationdate));
        return tokenDao.addToken(tokenDTO);
    }

}
