package com.test.jm.dto;

import com.test.jm.dto.test.CaseDTO;

import java.util.List;

public class CaseExtend {
    private String project_name;
    private List<CaseDTO> caseExtends;

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
