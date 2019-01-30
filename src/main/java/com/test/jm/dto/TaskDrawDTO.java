package com.test.jm.dto;

public class TaskDrawDTO {
    private Integer id;
    private String types;
    private String values;
    private String left;
    private String right;

    public TaskDrawDTO() {
    }

    public TaskDrawDTO(Integer id, String types, String values, String left, String right) {
        this.id = id;
        this.types = types;
        this.values = values;
        this.left = left;
        this.right = right;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "TaskDrawDTO{" +
                "id='" + id + '\'' +
                ", types='" + types + '\'' +
                ", values='" + values + '\'' +
                ", left='" + left + '\'' +
                ", right='" + right + '\'' +
                '}';
    }
}
