package com.test.jm.domain;

public class TaskParams {
    private String run_type;
    private String task_id;

    public TaskParams() {
    }

    public TaskParams(String run_type, String task_id) {
        this.run_type = run_type;
        this.task_id = task_id;
    }

    public String getRun_type() {
        return run_type;
    }

    public void setRun_type(String run_type) {
        this.run_type = run_type;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }
}
