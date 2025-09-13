package com.fosscut.type.cutting.plan;

public class PlanOutput {

    private Integer id;
    private Integer count;
    private Integer relax;

    public PlanOutput() {}
    public PlanOutput(Integer id, Integer count, Integer relax) {
        this.id = id;
        this.count = count;
        this.relax = relax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(Integer relax) {
        this.relax = relax;
    }

    @Override
    public String toString() {
        return "PlanOutput: id=" + this.getId() + ", count=" + this.getCount() + ", relax=" + this.getRelax();
    }

}
