package com.test.jm.domain;

import java.util.List;

public class TaskExtendResult<T> {
    private String id;
    private String status;
    private String msg;
    private List<T> data;

    public TaskExtendResult() {
    }

    public TaskExtendResult(String status, String msg, List<T> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public TaskExtendResult(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public TaskExtendResult(String id, String status, String msg) {
        this.id = id;
        this.status = status;
        this.msg = msg;
    }

    public TaskExtendResult(String id, String status, String msg, List<T> data) {
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TaskExtendResult{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
