package com.test.jm.dto.test;

import java.time.LocalDateTime;

public class CaseDTO {

    private Integer id;
    private String project_id;
    private String case_name;
    private String case_list;
    private Integer status;
    private String author;
    private String update_author;
    private LocalDateTime create_time;
    private LocalDateTime modify_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCase_name() {
        return case_name;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public String getCase_list() {
        return case_list;
    }

    public void setCase_list(String case_list) {
        this.case_list = case_list;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CaseDTO{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", case_name='" + case_name + '\'' +
                ", case_list='" + case_list + '\'' +
                ", status=" + status +
                ", author='" + author + '\'' +
                ", update_author='" + update_author + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
