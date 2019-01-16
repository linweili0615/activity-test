package com.test.jm.domain.swagger;

public class ReqInfo {
    private String project_id;
    private String url;

    public ReqInfo() {
    }

    public ReqInfo(String project_id, String url) {
        this.project_id = project_id;
        this.url = url;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ReqInfo{" +
                "project_id='" + project_id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
