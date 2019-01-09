package com.test.jm.domain.swagger;

import java.util.List;
import java.util.Map;

public class JsonSwagger<T> {
    private String basePath;
    private String host;
    private String swagger;
    private Object info;
    private Object definitions;
    private List<Group> tags;
    private Map<String, T> paths;

    public JsonSwagger() {
    }

    public JsonSwagger(String basePath, String host, String swagger, Object info, Object definitions, List<Group> tags, Map<String, T> paths) {
        this.basePath = basePath;
        this.host = host;
        this.swagger = swagger;
        this.info = info;
        this.definitions = definitions;
        this.tags = tags;
        this.paths = paths;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Object getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Object definitions) {
        this.definitions = definitions;
    }

    public List<Group> getTags() {
        return tags;
    }

    public void setTags(List<Group> tags) {
        this.tags = tags;
    }

    public Map<String, T> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, T> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return "JsonSwagger{" +
                "basePath='" + basePath + '\'' +
                ", host='" + host + '\'' +
                ", swagger='" + swagger + '\'' +
                ", info=" + info +
                ", definitions=" + definitions +
                ", tags=" + tags +
                ", paths=" + paths +
                '}';
    }
}
