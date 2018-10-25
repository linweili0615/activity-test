package com.test.jm.dto;

import java.time.LocalDateTime;

public class TokenDTO {

    private Integer id;
    private String user_id;
    private String token;
    private String status;
    private LocalDateTime expire_time;
    private LocalDateTime create_time;
    private LocalDateTime modify_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(LocalDateTime expire_time) {
        this.expire_time = expire_time;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public LocalDateTime getModify_time() {
        return modify_time;
    }

    public void setModify_time(LocalDateTime modify_time) {
        this.modify_time = modify_time;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", expire_time=" + expire_time +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
