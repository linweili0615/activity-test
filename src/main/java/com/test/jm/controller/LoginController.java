package com.test.jm.controller;

import com.test.jm.dto.UserInfoDTO;
import com.test.jm.service.LoginService;
import com.test.jm.service.UserInfoService;
import com.test.jm.util.CookieUtils;
import com.test.jm.util.Md5Util;
import com.test.jm.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@Controller
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LoginService loginService;

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

    @GetMapping("/2login")
    @ResponseBody
    public String login2(HttpServletResponse response){

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setTelno("15866660001");
        userInfoDTO.setPwd(Md5Util.encoder("pwd"));
        UserInfoDTO uu = userInfoService.getUserInfo(userInfoDTO);
        if(uu != null){
            String token =  TokenUtils.createJwtToken(uu.getId(), 60);
            CookieUtils.writeCookie(response,"jm", token);
            return "ok,检查下cookie";
        }else {
            System.out.println("用户名或密码错误");
            return "用户名或密码错误";
        }

    }



}
