package com.test.jm;

import com.test.jm.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmApplicationTests {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void contextLoads(){
        String id = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(id,"测试下");
        System.out.printf(redisTemplate.opsForValue().get(id).toString());

    }

    @Test
    public void rutil(){
        String id = UUID.randomUUID().toString();
        redisUtil.set(id,"测试下");
        System.out.printf("id: " + id + "==>" + redisTemplate.opsForValue().get(id).toString());

    }

}
