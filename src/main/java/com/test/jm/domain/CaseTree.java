package com.test.jm.domain;

import com.test.jm.dto.test.CaseDTO;

import java.util.List;

public class CaseTree {
    private String id;
    private String name;
    private List<CaseDTO> children;

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

    public List<CaseDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CaseDTO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "CaseTree{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}
