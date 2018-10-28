package com.test.jm.config.interceptors;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.util.CookieUtils;
import com.test.jm.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenDao tokenDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jm_cookie = CookieUtils.getCookie(request,"jm");
        if(StringUtils.isNotBlank(jm_cookie)){
            try {
                Claims claims = TokenUtils.parseJWTToken(jm_cookie);
                String userid = (String) claims.get("userid");
                TokenDTO tokenDTO = tokenDao.findTokenByUserId(userid);

                //数据库没有token记录
                if(null == tokenDTO){
                    return false;
                }

                //数据库token与客户端token比较
                if(!jm_cookie.equals(tokenDTO.getToken())){
                    return false;
                }

                //判断token过期
                Date tokenDate = claims.getExpiration();
                if(!tokenDate.after(new Date())){
                    return false;
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }


        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }
}
