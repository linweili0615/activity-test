package com.test.jm.dto;

public class TaskDrawDTO {
    private Integer id;
    private Integer extend_id;
    private String types;
    private String values;
    private String left;
    private String right;

    public TaskDrawDTO() {
    }

    public TaskDrawDTO(Integer extend_id, String types, String values, String left, String right) {
        this.extend_id = extend_id;
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

    public Integer getExtend_id() {
        return extend_id;
    }

    public void setExtend_id(Integer extend_id) {
        this.extend_id = extend_id;
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
                "id=" + id +
                ", extend_id='" + extend_id + '\'' +
                ", types='" + types + '\'' +
                ", values='" + values + '\'' +
                ", left='" + left + '\'' +
                ", right='" + right + '\'' +
                '}';
    }
}
