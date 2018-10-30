package com.test.jm.controller;

import com.test.jm.dto.UserInfoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/user")
@Controller
public class LoginController {

    @RequestMapping("/login")
    @ResponseBody
    public String login(){
        return "login ok le";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test1(){
        return "login test";
    }

    @PostMapping("/2login")
    public String login2(@RequestBody UserInfoDTO userInfoDTO){

        return null;
    }



}
