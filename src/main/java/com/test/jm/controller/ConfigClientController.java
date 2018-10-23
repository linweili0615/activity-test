package com.test.jm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {
    @Value("${test.appname}")
    private String name;

    @GetMapping("/test123")
    public String getNames(){
        return "获取到git配置文件属性：" + name;
    }
}