package com.test.jm.domain;

import java.io.Serializable;

public class HttpClientResult implements Serializable {

    private String start_time;
    private String end_time;
    private String api_id;
    private String api_name;
    private String req_url;
    private String req_method;
    private String req_cookies;
    private String req_headers;
    private String req_body;
    private Integer res_code;
    private String res_cookies;
    private String res_headers;
    private String res_body;

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

    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getReq_method() {
        return req_method;
    }

    public void setReq_method(String req_method) {
        this.req_method = req_method;
    }

    public String getReq_cookies() {
        return req_cookies;
    }

    public void setReq_cookies(String req_cookies) {
        this.req_cookies = req_cookies;
    }

    public String getReq_headers() {
        return req_headers;
    }

    public void setReq_headers(String req_headers) {
        this.req_headers = req_headers;
    }

    public String getReq_body() {
        return req_body;
    }

    public void setReq_body(String req_body) {
        this.req_body = req_body;
    }

    public Integer getRes_code() {
        return res_code;
    }

    public void setRes_code(Integer res_code) {
        this.res_code = res_code;
    }

    public String getRes_cookies() {
        return res_cookies;
    }

    public void setRes_cookies(String res_cookies) {
        this.res_cookies = res_cookies;
    }

    public String getRes_headers() {
        return res_headers;
    }

    public void setRes_headers(String res_headers) {
        this.res_headers = res_headers;
    }

    public String getRes_body() {
        return res_body;
    }

    public void setRes_body(String res_body) {
        this.res_body = res_body;
    }

    @Override
    public String toString() {
        return "HttpClientResult{" +
                "start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", api_id='" + api_id + '\'' +
                ", api_name='" + api_name + '\'' +
                ", req_url='" + req_url + '\'' +
                ", req_method='" + req_method + '\'' +
                ", req_cookies='" + req_cookies + '\'' +
                ", req_headers='" + req_headers + '\'' +
                ", req_body='" + req_body + '\'' +
                ", res_code=" + res_code +
                ", res_cookies='" + res_cookies + '\'' +
                ", res_headers='" + res_headers + '\'' +
                ", res_body='" + res_body + '\'' +
                '}';
    }
}
