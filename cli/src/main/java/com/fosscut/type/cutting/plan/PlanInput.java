package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.shared.type.cutting.Element;

public class PlanInput extends Element {

    private Double cost;
    private List<Pattern> patterns;

    public PlanInput() {}

    public PlanInput(Integer length) {
        super(length);
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

}
