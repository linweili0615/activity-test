package com.test.jm.dto.job;

public class Qrtz_Scheduler_State {
    private String sched_name;
    private String trigger_name;
    private String trigger_group;
    private String job_name;
    private String job_group;
    private String description;
    private String next_fire_time;
    private String prev_fire_time;
    private String priority;
    private String trigger_state;
    private String trigger_type;
    private String start_time;
    private String end_time;
    private String calendar_name;
    private String misfire_instr;
    private String job_data;

    public String getSched_name() {
        return sched_name;
    }

    public void setSched_name(String sched_name) {
        this.sched_name = sched_name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNext_fire_time() {
        return next_fire_time;
    }

    public void setNext_fire_time(String next_fire_time) {
        this.next_fire_time = next_fire_time;
    }

    public String getPrev_fire_time() {
        return prev_fire_time;
    }

    public void setPrev_fire_time(String prev_fire_time) {
        this.prev_fire_time = prev_fire_time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTrigger_state() {
        return trigger_state;
    }

    public void setTrigger_state(String trigger_state) {
        this.trigger_state = trigger_state;
    }

    public String getTrigger_type() {
        return trigger_type;
    }

    public void setTrigger_type(String trigger_type) {
        this.trigger_type = trigger_type;
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

    public String getCalendar_name() {
        return calendar_name;
    }

    public void setCalendar_name(String calendar_name) {
        this.calendar_name = calendar_name;
    }

    public String getMisfire_instr() {
        return misfire_instr;
    }

    public void setMisfire_instr(String misfire_instr) {
        this.misfire_instr = misfire_instr;
    }

    public String getJob_data() {
        return job_data;
    }

    public void setJob_data(String job_data) {
        this.job_data = job_data;
    }
}
