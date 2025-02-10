package com.fosscut.type.cutting;

import com.fosscut.shared.type.cutting.Element;
import com.fosscut.type.cutting.plan.PlanOutput;
import com.fosscut.type.cutting.plan.PlanOutputDouble;
import com.fosscut.type.cutting.plan.PlanOutputInteger;

/*
 * Constructive heuristic output.
 */
public class CHOutput extends Element {

    private Integer id;
    private Integer count;
    private Double relax;

    public CHOutput(Integer id, Integer length, Integer count, Double relax) {
        this.id = id;
        this.setLength(length);
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

    public Double getRelax() {
        return relax;
    }

    public void setRelax(Double relax) {
        this.relax = relax;
    }

    public PlanOutput getPlanOutput() {
        return new PlanOutput(
            this.getId(),
            this.getCount()
        );
    }

    public PlanOutputDouble getPlanOutputDouble() {
        return new PlanOutputDouble(
            this.getId(),
            this.getCount(),
            this.getRelax()
        );
    }

    public PlanOutputInteger getPlanOutputInteger() {
        return new PlanOutputInteger(
            this.getId(),
            this.getCount(),
            this.getRelax().intValue()
        );
    }

}
