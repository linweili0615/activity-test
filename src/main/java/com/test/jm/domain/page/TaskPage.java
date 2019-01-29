package com.test.jm.domain.page;

public class TaskPage {
    private Integer pageSize;
    private Integer pageNo;
    private String id;
    private String name;

    public TaskPage() {
    }

    public TaskPage(Integer pageSize, Integer pageNo, String id, String name) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.id = id;
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
