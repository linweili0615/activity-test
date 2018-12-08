package com.test.jm.domain.tree;

public class Case {
    private String id;
    private String label;

    public Case() {
    }

    public Case(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Case{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
