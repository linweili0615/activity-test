package com.test.jm.domain;

import com.test.jm.dto.test.TaskExtendDTO;

import java.util.List;

public class TaskExtendResult {
    private String id;
    private String status;
    private String msg;
    private List<TaskExtendDTO> data;

    public TaskExtendResult() {
    }

    public TaskExtendResult(String id, String status, String msg, List<TaskExtendDTO> data) {
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

    public List<TaskExtendDTO> getData() {
        return data;
    }

    public void setData(List<TaskExtendDTO> data) {
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
