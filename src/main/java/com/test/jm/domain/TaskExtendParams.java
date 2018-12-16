package com.test.jm.domain;

import com.test.jm.dto.test.ApiDTO;

import java.util.List;

public class TaskExtendParams {

    private List<ApiDTO> list;
    private String task_id;

    public TaskExtendParams() {
    }

    public TaskExtendParams(List<ApiDTO> list, String task_id) {
        this.list = list;
        this.task_id = task_id;
    }

    public List<ApiDTO> getList() {
        return list;
    }

    public void setList(List<ApiDTO> list) {
        this.list = list;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    @Override
    public String toString() {
        return "TaskExtendParams{" +
                "list=" + list +
                ", task_id='" + task_id + '\'' +
                '}';
    }
}
