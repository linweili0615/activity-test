package com.test.jm.controller;

import com.test.jm.dto.UserInfoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        return "login.html";
    }

    @PostMapping("/2login")
    public String login2(@RequestBody UserInfoDTO userInfoDTO){

        return null;
    }

}
