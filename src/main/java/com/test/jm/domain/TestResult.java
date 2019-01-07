package com.test.jm.domain;

public class TestResult {
    private String id;
    private String status;
    private String msg;
    private Object data;


    public TestResult(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public TestResult(String id, String status, String msg, Object data) {
        this.id = id;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public TestResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
