package com.fosscut.type.cutting.plan;

public class PlanOutputDouble extends PlanOutput {
    private Double relax;

    public PlanOutputDouble(Integer id, Integer number, Double relax) {
        super(id, number);
        this.relax = relax;
    }

    public Double getRelax() {
        return relax;
    }

    public void setRelax(Double relax) {
        this.relax = relax;
    }
}
