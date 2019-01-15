package com.test.jm.domain;

import com.test.jm.dto.ProjectDTO;

import java.util.List;

public class ProjectResult {

    private String status;
    private String msg;
    private long total;
    private long pageSize;
    private long pageCount;
    private long pageNo;

    private List<ProjectDTO> projectDTOList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProjectDTO> getProjectDTOList() {
        return projectDTOList;
    }

    public void setProjectDTOList(List<ProjectDTO> projectDTOList) {
        this.projectDTOList = projectDTOList;
    }

    @Override
    public String toString() {
        return "ProjectResult{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", total=" + total +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", pageNo=" + pageNo +
                ", projectDTOList=" + projectDTOList +
                '}';
    }
}
