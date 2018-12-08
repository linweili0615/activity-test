package com.test.jm.domain.page;

public class ProjectPage {
    private Integer pageSize;
    private Integer pageNo;
    private String id;
    private String project_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "ProjectPage{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", id='" + id + '\'' +
                ", project_name='" + project_name + '\'' +
                '}';
    }
}
