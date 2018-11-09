package com.test.jm.domain;

public class TestResult {
    private String id;
    private Integer status;
    private String test_result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", test_result='" + test_result + '\'' +
                '}';
    }
}
