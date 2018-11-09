package com.test.jm.domain;

public class TestResult {
    private String id;
    private String status;
    private String resheaders;
    private String resbody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResheaders() {
        return resheaders;
    }

    public void setResheaders(String resheaders) {
        this.resheaders = resheaders;
    }

    public String getResbody() {
        return resbody;
    }

    public void setResbody(String resbody) {
        this.resbody = resbody;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", resheaders='" + resheaders + '\'' +
                ", resbody='" + resbody + '\'' +
                '}';
    }
}
