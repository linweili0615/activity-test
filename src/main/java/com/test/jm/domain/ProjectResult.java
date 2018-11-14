package com.test.jm.domain;

import com.test.jm.dto.test.ProjectDTO;

import java.util.List;

public class ProjectResult {

    private String status;
    private String msg;
    private Integer total;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
                ", projectDTOList=" + projectDTOList +
                '}';
    }
}
