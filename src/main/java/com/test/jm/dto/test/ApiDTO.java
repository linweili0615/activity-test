package com.test.jm.dto.test;

import java.time.LocalDateTime;

public class ApiDTO {
    private String id;
    private String project_id;
    private String case_id;
    private Integer case_index;
    private String name;
    private String url;
    private String headers;
    private String cookies;
    private String method;
    private String body;
    private String paramstype;
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

    public Integer getCase_index() {
        return case_index;
    }

    public void setCase_index(Integer case_index) {
        this.case_index = case_index;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public String getParamstype() {
        return paramstype;
    }

    public void setParamstype(String paramstype) {
        this.paramstype = paramstype;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ApiDTO{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", case_id='" + case_id + '\'' +
                ", case_index='" + case_index + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", headers='" + headers + '\'' +
                ", cookies='" + cookies + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", paramstype='" + paramstype + '\'' +
                ", author='" + author + '\'' +
                ", update_author='" + update_author + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
