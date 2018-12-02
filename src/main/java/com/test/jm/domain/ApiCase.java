package com.test.jm.domain;

public class ApiCase {
    private String value;
    private String label;

    public ApiCase() {
    }

    public ApiCase(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ApiCase{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
