package com.test.jm.domain;

import java.util.List;

public class TaskResult {
    private Integer total;
    private Integer success;
    private Integer fail;
    private String percent;
    private String start_time;
    private long consuming_time;
    private String end_time;
    private String executor;
    private List<HttpClientResult> resultList;

    public TaskResult() {
    }

    public TaskResult(Integer total, Integer success, Integer fail, String percent, String start_time, long consuming_time, String end_time, String executor, List<HttpClientResult> resultList) {
        this.total = total;
        this.success = success;
        this.fail = fail;
        this.percent = percent;
        this.start_time = start_time;
        this.consuming_time = consuming_time;
        this.end_time = end_time;
        this.executor = executor;
        this.resultList = resultList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public List<HttpClientResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<HttpClientResult> resultList) {
        this.resultList = resultList;
    }

    public long getConsuming_time() {
        return consuming_time;
    }

    public void setConsuming_time(long consuming_time) {
        this.consuming_time = consuming_time;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "total=" + total +
                ", success=" + success +
                ", fail=" + fail +
                ", percent='" + percent + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", executor='" + executor + '\'' +
                ", resultList=" + resultList +
                '}';
    }
}
