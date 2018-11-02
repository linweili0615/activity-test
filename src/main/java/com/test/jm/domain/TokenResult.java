package com.test.jm.domain;

import com.test.jm.dto.TokenDTO;

public class TokenResult{
    private String code;
    private String msg;
    private String user_id;
    private String user_name;
    private String token;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "TokenResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
