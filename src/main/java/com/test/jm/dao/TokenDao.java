package com.test.jm.dao;

import com.test.jm.dto.TokenDTO;

public interface TokenDao {

    Integer addToken(TokenDTO tokenDTO);

    Integer updateToken(TokenDTO tokenDTO);

    TokenDTO findTokenByUserId(String user_id);

}
