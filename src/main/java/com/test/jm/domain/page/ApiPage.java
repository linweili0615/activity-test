package com.test.jm.domain.page;

public class ApiPage {
    private Integer pageSize;
    private Integer pageNo;
    private String project_id;
    private String case_id;
    private String name;

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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ApiPage{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", project_id='" + project_id + '\'' +
                ", case_id='" + case_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
