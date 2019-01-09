package com.test.jm.domain.swagger;

import java.util.List;

public class Info {
    private String summary;
    private List produces;
    private String description;
    private String operationId;
    private Object responses;
    private List tags;
    private List consumes;

    public Info() {
    }

    public Info(String summary, List produces, String description, String operationId, Object responses, List tags, List consumes) {
        this.summary = summary;
        this.produces = produces;
        this.description = description;
        this.operationId = operationId;
        this.responses = responses;
        this.tags = tags;
        this.consumes = consumes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List getProduces() {
        return produces;
    }

    public void setProduces(List produces) {
        this.produces = produces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Object getResponses() {
        return responses;
    }

    public void setResponses(Object responses) {
        this.responses = responses;
    }

    public List getTags() {
        return tags;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public List getConsumes() {
        return consumes;
    }

    public void setConsumes(List consumes) {
        this.consumes = consumes;
    }

    @Override
    public String toString() {
        return "Info{" +
                "summary='" + summary + '\'' +
                ", produces=" + produces +
                ", description='" + description + '\'' +
                ", operationId='" + operationId + '\'' +
                ", responses=" + responses +
                ", tags=" + tags +
                ", consumes=" + consumes +
                '}';
    }
}
