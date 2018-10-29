package com.test.jm.config.interceptors;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.util.CookieUtils;
import com.test.jm.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private TokenDao tokenDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jm_cookie = CookieUtils.getCookie(request,"jm");
        Claims claims;
        if(StringUtils.isNotBlank(jm_cookie)){
            try {
                claims = TokenUtils.parseJWTToken(jm_cookie);
                String userid = claims.getId();
                TokenDTO tokenDTO = tokenDao.findTokenByUserId(userid);
                //数据库没有token记录
                if(null == tokenDTO){
                    logger.info("数据库没有该用户token记录：userid:{}, token:{}", userid, jm_cookie);
                    return false;
                }
                //数据库token与客户端token比较
                if(!jm_cookie.equals(tokenDTO.getToken())){
                    logger.info("数据库token与客户端token不一致：userid{}, token:{}", userid, jm_cookie);
                    return false;
                }
                //判断token过期
                Date tokenDate = claims.getExpiration();
                if(!tokenDate.after(new Date())){
                    logger.info("该用户token信息已过期：userid{}, token:{}", userid, jm_cookie);
                    return false;
                }
                logger.info("token信息校验通过：userid{}, token:{}", userid, jm_cookie);

            }catch (Exception e){
                logger.info("该token信息已校验异常: {}", jm_cookie);
                e.printStackTrace();
                return false;
            }

            logger.info("该token信息验证通过: {}", jm_cookie);
            return true;

        }else {
            logger.info("token信息不能为空");
            return false;
        }



    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }
}
