package com.test.jm.dto;

public class TaskResultDTO {
    private String id;
    private String task_id;
    private String u_id;
    private String u_name;
    private String execute_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }
}
