package com.fosscut.type.cutting.plan;

import java.util.List;

public class Pattern {
    private Integer number;
    private List<PlanOutput> patternDefinition;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<PlanOutput> getPatternDefinition() {
        return patternDefinition;
    }

    public void setPatternDefinition(List<PlanOutput> patternDefinition) {
        this.patternDefinition = patternDefinition;
    }
}
