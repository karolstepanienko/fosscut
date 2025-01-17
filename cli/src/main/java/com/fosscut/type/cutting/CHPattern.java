package com.fosscut.type.cutting;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.plan.PlanOutput;

/*
 * Constructive heuristic pattern.
 */
public class CHPattern {

    private OrderInput input;
    private Integer count;
    private List<CHOutput> patternDefinition;

    public void setInput(OrderInput input) {
        this.input = input;
    }

    public OrderInput getInput() {
        return input;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CHOutput> getPatternDefinition() {
        return patternDefinition;
    }

    public void setPatternDefinition(List<CHOutput> patternDefinition) {
        this.patternDefinition = patternDefinition;
    }

    public Integer getWaist() {
        Integer outputsSumLength = 0;
        for (CHOutput ffdOutput : patternDefinition) {
            Integer outputSumLength = ffdOutput.getLength() - ffdOutput.getRelax();
            outputsSumLength += ffdOutput.getCount() * outputSumLength;
        }
        return this.input.getLength() - outputsSumLength;
    }

    public List<PlanOutput> getSerialisableRelaxPatternDefinition() {
        List<PlanOutput> serialisablePatternDefinition = new ArrayList<PlanOutput>();
        for (CHOutput ffdOutput : patternDefinition) {
            serialisablePatternDefinition.add(ffdOutput.getPlanOutputInteger());
        }
        return serialisablePatternDefinition;
    }

}
