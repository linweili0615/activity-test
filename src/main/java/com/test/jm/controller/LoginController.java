package com.test.jm.controller;

import com.test.jm.domain.TokenResult;
import com.test.jm.dto.TokenDTO;
import com.test.jm.dto.UserInfoDTO;
import com.test.jm.keys.ResultType;
import com.test.jm.service.LoginService;
import com.test.jm.service.TokenService;
import com.test.jm.service.UserInfoService;
import com.test.jm.util.CookieUtils;
import com.test.jm.util.Md5Util;
import com.test.jm.util.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        return "index.html";
    }

    @PostMapping("/validtoken")
    @ResponseBody
    public TokenResult validtoken(HttpServletRequest request){
        TokenDTO tokenDTO = loginService.validToken(request);
        if( tokenDTO != null){
            return new TokenResult(ResultType.LOGIN, "已登录", tokenDTO.getUser_id(),tokenDTO.getUser_name(),tokenDTO.getToken());
        }
       return new TokenResult(ResultType.NOT_LOGIN, "未登录");
    }

    @PostMapping("/2login")
    @ResponseBody
    public TokenResult login2(HttpServletResponse response,
                              @RequestBody UserInfoDTO  userInfoDTO){
        if(StringUtils.isBlank(userInfoDTO.getTelno())&& StringUtils.isBlank(userInfoDTO.getPwd())) {
            return new TokenResult(ResultType.FAIL,"用户名或密码不能为空");
        }
        userInfoDTO.setPwd(Md5Util.encoder(userInfoDTO.getPwd()));
        UserInfoDTO userInfo = userInfoService.getUserInfo(userInfoDTO);
        if(userInfo == null){
            return new TokenResult(ResultType.FAIL,"用户名或密码错误");
        }
        long expirationdate = 24;
        String token =  TokenUtils.createJwtToken(userInfo.getId(), expirationdate);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUser_name(userInfo.getUsername());
        tokenDTO.setUser_id(userInfo.getId());
        tokenDTO.setToken(token);
        try {
            Integer count = tokenService.addToken(tokenDTO, expirationdate);
            if(count > 0){
                Cookie cookie = new Cookie("jm",token);
                cookie.setPath("/");
                //设置Cookie的有效期为1天
                cookie.setMaxAge(60*60*24*7);
                response.addCookie(cookie);
            }
            return new TokenResult(ResultType.LOGIN,"获取token成功",tokenDTO.getUser_id(),userInfo.getUsername(),token);
        } catch (Exception e) {
            logger.info("token写入异常");
            e.printStackTrace();
            return new TokenResult(ResultType.FAIL,e.getMessage());
        }
    }

    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletResponse response){
        CookieUtils.removeCookie(response,"jm");
    }

}
