package com.test.jm.dto.test;

import java.time.LocalDateTime;

public class CaseDTO {

    private String id;
    private String project_id;
    private String name;
    private String author;
    private String update_author;
    private LocalDateTime create_time;
    private LocalDateTime modify_time;

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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getUpdate_author() {
        return update_author;
    }

    public void setUpdate_author(String update_author) {
        this.update_author = update_author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        return "CaseDTO{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", update_author='" + update_author + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
