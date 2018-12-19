package com.test.jm.domain;

public class TaskExtendRank {
    private Integer old_id;
    private Integer old_rank;
    private Integer new_id;
    private Integer new_rank;

    public Integer getOld_id() {
        return old_id;
    }

    public void setOld_id(Integer old_id) {
        this.old_id = old_id;
    }

    public Integer getOld_rank() {
        return old_rank;
    }

    public void setOld_rank(Integer old_rank) {
        this.old_rank = old_rank;
    }

    public Integer getNew_id() {
        return new_id;
    }

    public void setNew_id(Integer new_id) {
        this.new_id = new_id;
    }

    public Integer getNew_rank() {
        return new_rank;
    }

    public void setNew_rank(Integer new_rank) {
        this.new_rank = new_rank;
    }

    @Override
    public String toString() {
        return "TaskExtendRank{" +
                "old_id='" + old_id + '\'' +
                ", old_rank=" + old_rank +
                ", new_id='" + new_id + '\'' +
                ", new_rank='" + new_rank + '\'' +
                '}';
    }
}
