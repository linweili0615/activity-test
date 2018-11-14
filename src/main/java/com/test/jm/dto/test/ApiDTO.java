package com.test.jm.dto.test;

import java.time.LocalDateTime;

public class ApiDTO {
    private String id;
    private String url;
    private String headers;
    private String method;
    private String body;
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

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
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

    @Override
    public String toString() {
        return "ApiDTO{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", headers='" + headers + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", author='" + author + '\'' +
                ", update_author='" + update_author + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                '}';
    }
}
