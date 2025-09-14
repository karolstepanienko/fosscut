package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.shared.type.cutting.order.OrderOutput;

public class Pattern {

    private Integer count;
    private List<PlanOutput> patternDefinition;

    public Pattern() {}

    public Pattern(Integer count, List<PlanOutput> patternDefinition) {
        this.count = count;
        this.patternDefinition = patternDefinition;
    }

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

    public Integer getTotalOutputsLength(List<OrderOutput> orderOutputs) {
        int totalLength = 0;
        for (PlanOutput planOutput : patternDefinition) {
            Integer outputLength = orderOutputs.get(planOutput.getId()).getLength();
            if (planOutput.getRelax() != null) {
                outputLength -= planOutput.getRelax();
            }
            totalLength += planOutput.getCount() * outputLength;
        }
        return totalLength;
    }

}
