package com.test.jm.dto.test;

import java.time.LocalDateTime;

public class TaskExtendDTO {
    private String id;
    private String task_id;
    private String api_id;
    private String api_name;
    private Integer rank;
    private String pre_processors;
    private String post_processors;
    private String valid_data;
    private String status;
    private LocalDateTime create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getApi_id() {
        return api_id;
    }

    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getPre_processors() {
        return pre_processors;
    }

    public void setPre_processors(String pre_processors) {
        this.pre_processors = pre_processors;
    }

    public String getPost_processors() {
        return post_processors;
    }

    public void setPost_processors(String post_processors) {
        this.post_processors = post_processors;
    }

    public String getValid_data() {
        return valid_data;
    }

    public void setValid_data(String valid_data) {
        this.valid_data = valid_data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "TaskExtendDTO{" +
                "id='" + id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", api_id='" + api_id + '\'' +
                ", api_name='" + api_name + '\'' +
                ", rank=" + rank +
                ", pre_processors='" + pre_processors + '\'' +
                ", post_processors='" + post_processors + '\'' +
                ", valid_data='" + valid_data + '\'' +
                ", status='" + status + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}