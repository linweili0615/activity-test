package com.test.jm.dto;

import java.time.LocalDateTime;

public class ProjectDTO {
    private String id;
    private String project_name;
    private Integer status;
    private String author;
    private String update_author;
    private String create_time;
    private String modify_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "id='" + id + '\'' +
                ", project_name='" + project_name + '\'' +
                ", status=" + status +
                ", author='" + author + '\'' +
                ", update_author='" + update_author + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
