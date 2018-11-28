package com.test.jm.controller;

import com.test.jm.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RefreshScope
public class ConfigClientController {
    private static Logger logger = LoggerFactory.getLogger(ConfigClientController.class);

    @Value("${test.appname}")
    private String name;

    @GetMapping("/test123")
    public String getNames(){
        return "获取到git配置文件属性：" + name;
    }


    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/test")
    public String getId(){
        String id = UUID.randomUUID().toString();
        redisUtil.set(id,"测试下");
        String name =redisUtil.get(id).toString();
        System.out.printf("id: " + id + "==>" + name);
        return "获取到git配置文件属性：" + name;
    }
}