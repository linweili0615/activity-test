package com.test.jm.domain;

import java.util.List;

public class CaseResult {
    private String status;
    private String msg;
    private List data;

    public CaseResult(String status, String msg, List data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CaseResult(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CaseResult{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
