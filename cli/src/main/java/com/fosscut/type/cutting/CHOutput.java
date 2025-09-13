package com.fosscut.type.cutting;

import com.fosscut.shared.type.cutting.Element;
import com.fosscut.type.cutting.plan.PlanOutput;

/*
 * Constructive heuristic output.
 */
public class CHOutput extends Element {

    private Integer id;
    private Integer count;
    private Integer relax;

    public CHOutput(Integer id, Integer length, Integer count, Integer relax) {
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

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(Integer relax) {
        this.relax = relax;
    }

    public PlanOutput getPlanOutput() {
        return new PlanOutput(
            this.getId(),
            this.getCount(),
            this.getRelax()
        );
    }

    @Override
    public String toString() {
        return "CHOutput: id=" + this.getId() + ", length=" + this.getLength() + ", count=" + this.getCount() + ", relax=" + this.getRelax();
    }

}
