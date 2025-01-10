package com.fosscut.type.cutting.plan;

import java.util.List;

public class Pattern {
    private Integer count;
    private List<PlanOutput> patternDefinition;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PlanOutput> getPatternDefinition() {
        return patternDefinition;
    }

    public void setPatternDefinition(List<PlanOutput> patternDefinition) {
        this.patternDefinition = patternDefinition;
    }
}
