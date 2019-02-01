package com.test.jm.dto;

import java.time.LocalDateTime;

public class TaskExtendDTO {
    private Integer id;
    private String task_id;
    private String project_id;
    private String case_id;
    private String api_id;
    private String api_name;
    private Integer rank;
    private String valid_data;
    private String status;
    private LocalDateTime create_time;

    public TaskExtendDTO() {
    }


    public TaskExtendDTO(String task_id) {
        this.task_id = task_id;
    }

    public TaskExtendDTO(Integer id, String task_id, String project_id, String case_id, String api_id, String api_name, Integer rank,String valid_data, String status, LocalDateTime create_time) {
        this.id = id;
        this.task_id = task_id;
        this.project_id = project_id;
        this.case_id = case_id;
        this.api_id = api_id;
        this.api_name = api_name;
        this.rank = rank;
        this.valid_data = valid_data;
        this.status = status;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
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
                ", project_id='" + project_id + '\'' +
                ", case_id='" + case_id + '\'' +
                ", api_id='" + api_id + '\'' +
                ", api_name='" + api_name + '\'' +
                ", rank=" + rank +
                ", valid_data='" + valid_data + '\'' +
                ", status='" + status + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
