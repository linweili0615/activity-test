package com.test.jm.controller;

import com.test.jm.domain.TokenResult;
import com.test.jm.dto.TokenDTO;
import com.test.jm.dto.UserInfoDTO;
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
    public TokenDTO validtoken(HttpServletRequest request){
        TokenDTO tokenDTO = loginService.validToken(request);
        if( tokenDTO != null){
            return tokenDTO;
        }
       return null;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test1(){
        return "login test";
    }

    @PostMapping("/2login")
    @ResponseBody
    public TokenResult login2(HttpServletResponse response,
                              @RequestBody UserInfoDTO  userInfoDTO){
        TokenResult tokenResult = new TokenResult();
        if(StringUtils.isBlank(userInfoDTO.getTelno())&& StringUtils.isBlank(userInfoDTO.getPwd())) {
            tokenResult.setCode("203");
            tokenResult.setMsg("用户名或密码不能为空");
            return tokenResult;
        }

        userInfoDTO.setPwd(Md5Util.encoder(userInfoDTO.getPwd()));
        UserInfoDTO uu = userInfoService.getUserInfo(userInfoDTO);
        if(uu == null){
            tokenResult.setCode("202");
            tokenResult.setMsg("用户名或密码错误");
            return tokenResult;
        }

        long expirationdate = 2;
        String token =  TokenUtils.createJwtToken(uu.getId(), expirationdate);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUser_name(uu.getUsername());
        tokenDTO.setUser_id(uu.getId());
        tokenDTO.setToken(token);
        try {
            Integer count = tokenService.addToken(tokenDTO, expirationdate);
            tokenResult.setCode("200");
            tokenResult.setMsg("获取token成功");
            tokenResult.setUser_id(tokenDTO.getUser_id());
            tokenResult.setUser_name(uu.getUsername());
            tokenResult.setToken(token);
            return tokenResult;
        } catch (Exception e) {
            logger.info("写入token异常");
            e.printStackTrace();
            tokenResult.setCode("201");
            tokenResult.setMsg("写入token异常");
            return tokenResult;

        }



    }

    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletResponse response){
        CookieUtils.removeCookie(response,"jm");
    }

}
