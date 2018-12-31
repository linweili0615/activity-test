package com.test.jm.dto;

public class Person {
    Integer nid;
    private String name;
    private String email;
    private String part_id;
    private String etra;

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getEtra() {
        return etra;
    }

    public void setEtra(String etra) {
        this.etra = etra;
    }
}
