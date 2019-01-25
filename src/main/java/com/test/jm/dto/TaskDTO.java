package com.test.jm.dto;

import java.time.LocalDateTime;

public class TaskDTO {
    private String id;
    private String name;
    private String cron_expression;
    private String start_time;
    private String end_time;
    private Integer status;
    private String author;
    private String update_author;
    private LocalDateTime create_time;
    private LocalDateTime modify_time;

    public TaskDTO() {
    }

    public TaskDTO(String id, String name, String cron_expression, String start_time, String end_time, Integer status, String author, String update_author, LocalDateTime create_time, LocalDateTime modify_time) {
        this.id = id;
        this.name = name;
        this.cron_expression = cron_expression;
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
        this.author = author;
        this.update_author = update_author;
        this.create_time = create_time;
        this.modify_time = modify_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdate_author() {
        return update_author;
    }

    public void setUpdate_author(String update_author) {
        this.update_author = update_author;
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
}
