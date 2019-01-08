package com.test.jm.dto;

public class APIvariables {
    private Integer id;
    private String VariableName;
    private String VariableValue;


    public APIvariables() {
    }

    public APIvariables(Integer id, String variableName, String variableValue) {
        this.id = id;
        VariableName = variableName;
        VariableValue = variableValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVariableName() {
        return VariableName;
    }

    public void setVariableName(String variableName) {
        VariableName = variableName;
    }

    public String getVariableValue() {
        return VariableValue;
    }

    public void setVariableValue(String variableValue) {
        VariableValue = variableValue;
    }

    @Override
    public String toString() {
        return "APIvariables{" +
                "id=" + id +
                ", VariableName='" + VariableName + '\'' +
                ", VariableValue='" + VariableValue + '\'' +
                '}';
    }
}
