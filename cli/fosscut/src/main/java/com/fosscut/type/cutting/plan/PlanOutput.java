package com.fosscut.type.cutting.plan;

public class PlanOutput {
    private Integer id;
    private Integer number;
    private Integer relax;

    public PlanOutput(Integer id, Integer number, Integer relax) {
        this.id = id;
        this.number = number;
        this.relax = relax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(Integer relax) {
        this.relax = relax;
    }
}
