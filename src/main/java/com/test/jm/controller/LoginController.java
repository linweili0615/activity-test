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
    @ResponseBody
    public String login(HttpServletRequest request){
        if(loginService.validToken(request)){
            return "login ok le";
        }
        return "hao hao qu login ba";
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
        if(userInfoDTO.getTelno() != null && userInfoDTO.getPwd() != null){
            userInfoDTO.setPwd(Md5Util.encoder(userInfoDTO.getPwd()));
            logger.info(userInfoDTO.toString());
            UserInfoDTO uu = userInfoService.getUserInfo(userInfoDTO);
            if(uu != null){
                long expirationdate = 60;
                String token =  TokenUtils.createJwtToken(uu.getId(), expirationdate);
                TokenDTO tokenDTO = new TokenDTO();
                tokenDTO.setUser_id(uu.getId());
                tokenDTO.setToken(token);
                try {
                    Integer count = tokenService.addToken(tokenDTO, expirationdate);
                    if(count>0){
//                    CookieUtils.writeCookie(response,"jm", token);
                        tokenResult.setCode("200");
                        tokenResult.setMsg("获取token成功");
                        tokenResult.setToken(token);
                        return tokenResult;
                    }
                } catch (Exception e) {
                    logger.info("写入token异常");
                    e.printStackTrace();

                    tokenResult.setCode("201");
                    tokenResult.setMsg("写入token异常");
                    return tokenResult;

                }

            }else {
                logger.info("用户名或密码错误");
                tokenResult.setCode("202");
                tokenResult.setMsg("用户名或密码错误");
                return tokenResult;
            }

        }
        logger.info("用户名或密码不能为空");
        tokenResult.setCode("203");
        tokenResult.setMsg("用户名或密码不能为空");
        return tokenResult;


    }



}
