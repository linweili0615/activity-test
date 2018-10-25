package com.test.jm.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class UserInfoDTO {

    private String id;
    private String username;
    private String telno;
    private Integer error_count;
    private Integer status;
    private LocalDateTime add_date;
    private LocalDateTime modify_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public Integer getError_count() {
        return error_count;
    }

    public void setError_count(Integer error_count) {
        this.error_count = error_count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getAdd_date() {
        return add_date;
    }

    public void setAdd_date(LocalDateTime add_date) {
        this.add_date = add_date;
    }

    public LocalDateTime getModify_date() {
        return modify_date;
    }

    public void setModify_date(LocalDateTime modify_date) {
        this.modify_date = modify_date;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", telno='" + telno + '\'' +
                ", error_count=" + error_count +
                ", status=" + status +
                ", add_date=" + add_date +
                ", modify_date=" + modify_date +
                '}';
    }
}
