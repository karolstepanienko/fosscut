package com.fosscut.type.cutting.ffd;

import com.fosscut.type.cutting.Element;
import com.fosscut.type.cutting.plan.PlanOutputInteger;

public class FFDOutput extends Element {

    private Integer id;
    private Integer count;
    private Integer relax;

    public FFDOutput(Integer id, Integer length, Integer count, Integer relax) {
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

    public PlanOutputInteger getPlanOutputInteger() {
        return new PlanOutputInteger(
            this.getId(),
            this.getCount(),
            this.getRelax()
        );
    }

}
