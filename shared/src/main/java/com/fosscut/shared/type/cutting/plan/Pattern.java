package com.fosscut.shared.type.cutting.plan;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same reference
        if (obj == null || getClass() != obj.getClass()) return false;
        Pattern pattern = (Pattern) obj;
        // compares only patternDefinition, not count
        return getSortedPatternDefinition().equals(pattern.getSortedPatternDefinition());
    }

    private List<PlanOutput> getSortedPatternDefinition() {
        patternDefinition.sort((o1, o2) -> {
            return o1.getId().compareTo(o2.getId());
        });
        return patternDefinition;
    }

    // hashCode() must be overridden when equals() is overridden
    @Override
    public int hashCode() {
        return Objects.hash(count, patternDefinition);
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
