package com.test.jm.domain;

import org.apache.http.Header;

import java.io.Serializable;

public class HttpClientResult implements Serializable {

    private Integer code;
    private String headers;
    private String cookies;
    private String content;

    public HttpClientResult() {
    }

    public HttpClientResult(Integer code, String headers, String content, String cookies) {
        this.code = code;
        this.headers = headers;
        this.content = content;
        this.cookies = cookies;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public String toString() {
        return "HttpClientResult{" +
                "code=" + code +
                ", headers='" + headers + '\'' +
                ", cookies='" + cookies + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
