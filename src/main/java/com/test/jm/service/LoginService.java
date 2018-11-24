package com.test.jm.service;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.util.CookieUtils;
import com.test.jm.util.TokenUtils;
import com.test.jm.util.UserThreadLocal;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private TokenDao tokenDao;

    public TokenDTO validToken(HttpServletRequest request){
        String jm_cookie = CookieUtils.getCookie(request,"jm");
        Claims claims;
        if(StringUtils.isBlank(jm_cookie)){
            return null;
        }

        try {
            claims = TokenUtils.parseJWTToken(jm_cookie);
            if(null == claims){
                return null;
            }
            String user_id = claims.getId();
            TokenDTO tokenDTO = tokenDao.findTokenByUserId(user_id);
            //数据库没有token记录
            if(null == tokenDTO){
                logger.info("token信息不存在：userid:{}, token:{}", user_id, jm_cookie);
                return null;
            }
            //数据库token与客户端token比较
            if(!jm_cookie.equals(tokenDTO.getToken())){
                logger.info("token信息不一致：userid{}, token:{}", user_id, jm_cookie);
                return null;
            }
            //判断token过期
            Date tokenDate = claims.getExpiration();
            if(!tokenDate.after(new Date())){
                logger.info("token信息已过期：userid{}, token:{}", user_id, jm_cookie);
                return null;
            }

            UserThreadLocal.setUserInfo(tokenDTO);
            return tokenDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("token校验异常");
            return null;
        }

    }
}
