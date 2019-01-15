package com.test.jm;

import com.test.jm.dao.TokenDao;
import com.test.jm.dto.TokenDTO;
import com.test.jm.dto.UserInfoDTO;
import com.test.jm.dto.CaseDTO;
import com.test.jm.service.CaseService;
import com.test.jm.service.TokenService;
import com.test.jm.service.UserInfoService;
import com.test.jm.util.Md5Util;
import com.test.jm.util.RedisUtil;
import com.test.jm.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

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
    private CaseService caseService;
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
    public void adduserinfo() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(UUID.randomUUID().toString());
        userInfoDTO.setUsername("test_user");
        userInfoDTO.setTelno("15866660001");
        userInfoDTO.setPwd(Md5Util.encoder("123456a"));
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

        String token = TokenUtils.createJwtToken("6455276a-6bbe-417a-a8e7-725ef5619d64",60);
        System.out.println("token: " + token);
        Claims claims = TokenUtils.parseJWTToken(token);
        String gettoken = claims.getId();
        System.out.println("gettoken: " + gettoken);

    }

    @Test
    public void addToken(){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId("536afd10-1632-4d38-bd93-f75125f25333");
        String token = TokenUtils.createJwtToken(userInfoDTO.getId(),60);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUser_id(userInfoDTO.getId());
        tokenDTO.setToken(token);
        Integer count = tokenService.addToken(tokenDTO,60);
        System.out.println("count: " + count);
    }

    @Test
    public void addCase(){
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setName("测试集一");
        caseDTO.setProject_id("2a6dfe5a-e80d-11e8-9ff0-0242ac110002");
        List<String> list = new ArrayList();
        list.add("5cacb0e4-a9db-4f0b-b012-1f3e1542c43d");
        list.add("d84cff70-a533-4027-a427-399c73bc4a2d");
        caseDTO.setAuthor("linweili");
        String count = caseService.addCase(caseDTO);
        System.out.println(count);
    }





}
