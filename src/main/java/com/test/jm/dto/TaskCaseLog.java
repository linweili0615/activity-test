package com.test.jm.dto;

import org.omg.PortableInterceptor.INACTIVE;

import java.time.LocalDateTime;

public class TaskCaseLog {
    private Integer id;
    private String task_id;
    private String u_id;
    private LocalDateTime create_time;

    public TaskCaseLog() {
    }



    public TaskCaseLog(String task_id, String u_id) {
        this.task_id = task_id;
        this.u_id = u_id;
    }

    public TaskCaseLog(Integer id, String task_id, String u_id, LocalDateTime create_time) {
        this.id = id;
        this.task_id = task_id;
        this.u_id = u_id;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }
}
