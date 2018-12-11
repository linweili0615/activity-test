package com.test.jm.domain;

public class Result {
    private String id;
    private String status;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(String id, String status, String msg, Object data) {
        this.id = id;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(String status, String msg, Object data) {
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
