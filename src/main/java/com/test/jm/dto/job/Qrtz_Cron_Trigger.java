package com.test.jm.dto.job;

public class Qrtz_Cron_Trigger {
    private String sched_name;
    private String trigger_name;
    private String trigger_group;
    private String cron_expression;
    private String time_zone_id;

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

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public String getTime_zone_id() {
        return time_zone_id;
    }

    public void setTime_zone_id(String time_zone_id) {
        this.time_zone_id = time_zone_id;
    }
}
