package com.test.jm.dto;

import java.util.List;

public class CaseExtend {
    private String pid;
    private String project_name;
    private List<CaseDTO> caseExtends;


    public CaseExtend() {
    }

    public CaseExtend(String pid, String project_name, List<CaseDTO> caseExtends) {
        this.pid = pid;
        this.project_name = project_name;
        this.caseExtends = caseExtends;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public List<CaseDTO> getCaseExtends() {
        return caseExtends;
    }

    public void setCaseExtends(List<CaseDTO> caseExtends) {
        this.caseExtends = caseExtends;
    }

    @Override
    public String toString() {
        return "CaseExtend{" +
                "project_name='" + project_name + '\'' +
                ", caseExtends=" + caseExtends +
                '}';
    }
}
