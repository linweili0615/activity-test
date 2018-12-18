package com.test.jm.domain;

import java.util.List;

public class TaskExtendStatusParams {

    private List<String> list;
    private Integer status;

    public TaskExtendStatusParams() {
    }

    public TaskExtendStatusParams(List<String> list, Integer status) {
        this.list = list;
        this.status = status;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskExtendStatusParams{" +
                "list=" + list +
                ", status=" + status +
                '}';
    }
}
