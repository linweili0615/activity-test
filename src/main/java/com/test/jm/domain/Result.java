package com.test.jm.domain;

public class Result {
    private String id;
    private String status;
    private String msg;
    private String data;

    public Result() {
    }

    public Result(String id, String status, String msg, String data) {
        this.id = id;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
