package com.test.jm;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.dto.UserInfoDTO;
import com.test.jm.service.TokenService;
import com.test.jm.service.UserInfoService;
import com.test.jm.util.RedisUtil;
import com.test.jm.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmApplicationTests {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private TokenService tokenService;

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

    @Test
    public void adduserinfo(){

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(UUID.randomUUID().toString());
        userInfoDTO.setUsername("linweili");
        userInfoDTO.setTelno("18814288784");
        Integer status = userInfoService.addUserInfo(userInfoDTO);
        System.out.println("status:" + status);
    }

    @Test
    public void getuserinfobyParams(){

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId("6455276a-6bbe-417a-a8e7-725ef5619d64");
        userInfoDTO.setUsername("linweili");
        userInfoDTO.setTelno("18814288784");

        UserInfoDTO status = userInfoService.getUserInfo(userInfoDTO);
        System.out.println("UserInfoDTO:" + status.toString());
    }

    @Test
    public void updateuserinfobyParams(){

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId("6455276a-6bbe-417a-a8e7-725ef5619d64");
        userInfoDTO.setError_count(3);
        userInfoDTO.setStatus(0);

        Integer status = userInfoService.updateUserInfo(userInfoDTO);
        System.out.println("status:" + status);
    }

    @Test
    public void getToken(){

        String token = TokenUtils.createJwtToken("6455276a-6bbe-417a-a8e7-725ef5619d64",30);
        System.out.println("token: " + token);
        Claims claims = TokenUtils.parseJWTToken(token);
        String gettoken = claims.getId();
        System.out.println("gettoken: " + gettoken);

    }

    @Test
    public void addToken(){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId("6455276a-6bbe-417a-a8e7-725ef5619d64");
        String token = TokenUtils.createJwtToken(userInfoDTO.getId(),30);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUser_id(userInfoDTO.getId());
        tokenDTO.setToken(token);
        Integer count = tokenService.addToken(userInfoDTO);
        System.out.println("count: " + count);
    }




}
