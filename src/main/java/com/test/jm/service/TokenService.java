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

    public Integer addToken(TokenDTO tokenDTO, long expirationdate){
        if(findTokenByUserId(tokenDTO.getUser_id())!=null){
            return tokenDao.updateToken(tokenDTO);
        }
        tokenDTO.setStatus("0");
        tokenDTO.setExpire_time(LocalDateTime.now().plusMinutes(expirationdate));
        return tokenDao.addToken(tokenDTO);
    }

}
