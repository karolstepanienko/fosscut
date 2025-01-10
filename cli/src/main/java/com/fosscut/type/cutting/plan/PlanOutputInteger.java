package com.fosscut.type.cutting.plan;

public class PlanOutputInteger extends PlanOutput {
    private Integer relax;

    public PlanOutputInteger(Integer id, Integer count, Integer relax) {
        super(id, count);
        this.relax = relax;
    }

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(Integer relax) {
        this.relax = relax;
    }
}
