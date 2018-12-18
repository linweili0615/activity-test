package com.test.jm.domain;

import java.util.List;

public class TaskExtendStatusParams {

    private List<String> list;
    private String status;

    public TaskExtendStatusParams() {
    }

    public TaskExtendStatusParams(List<String> list, String status) {
        this.list = list;
        this.status = status;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskExtendStatusParams{" +
                "list=" + list +
                ", status='" + status + '\'' +
                '}';
    }
}
