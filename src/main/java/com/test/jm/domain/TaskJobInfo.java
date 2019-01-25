package com.test.jm.domain;

public class TaskJobInfo {
    private String task_id;
    private String name;
    private String job_name;
    private String job_group;
    private String trigger_name;
    private String trigger_group;
    private String start_time;
    private String end_time;
    private String cron_expression;

    public TaskJobInfo() {
    }

    public TaskJobInfo(String task_id, String name, String job_name, String job_group, String trigger_name, String trigger_group, String start_time, String end_time, String cron_expression) {
        this.task_id = task_id;
        this.name = name;
        this.job_name = job_name;
        this.job_group = job_group;
        this.trigger_name = trigger_name;
        this.trigger_group = trigger_group;
        this.start_time = start_time;
        this.end_time = end_time;
        this.cron_expression = cron_expression;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_group() {
        return job_group;
    }

    public void setJob_group(String job_group) {
        this.job_group = job_group;
    }

    public String getTrigger_name() {
        return trigger_name;
    }

    public void setTrigger_name(String trigger_name) {
        this.trigger_name = trigger_name;
    }

    public String getTrigger_group() {
        return trigger_group;
    }

    public void setTrigger_group(String trigger_group) {
        this.trigger_group = trigger_group;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }
}
