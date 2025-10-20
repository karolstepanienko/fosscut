package com.fosscut.shared.type.cutting.plan;

import java.util.Objects;

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
    public boolean equals(Object obj) {
        if (this == obj) return true; // same reference
        if (obj == null || getClass() != obj.getClass()) return false;
        PlanOutput that = (PlanOutput) obj;
        if (this.relax == null && that.relax != null) return false;
        if (this.relax != null && that.relax == null) return false;
        if (this.relax == null && that.relax == null) {
            return id.equals(that.id) && count.equals(that.count);
        } else {
            return id.equals(that.id) && count.equals(that.count) && relax.equals(that.relax);
        }
    }

    // hashCode() must be overridden when equals() is overridden
    @Override
    public int hashCode() {
        return Objects.hash(id, count, relax);
    }

    @Override
    public String toString() {
        return "PlanOutput: id=" + this.getId() + ", count=" + this.getCount() + ", relax=" + this.getRelax();
    }

}
