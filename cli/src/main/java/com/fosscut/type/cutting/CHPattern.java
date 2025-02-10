package com.fosscut.type.cutting;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.plan.PlanOutput;

/*
 * Constructive heuristic pattern.
 */
public class CHPattern {

    private Integer inputId;
    private OrderInput input;
    private Integer count;
    private List<CHOutput> patternDefinition;

    public Integer getInputId() {
        return inputId;
    }

    public void setInputId(Integer inputId) {
        this.inputId = inputId;
    }

    public OrderInput getInput() {
        return input;
    }

    public void setInput(OrderInput input) {
        this.input = input;
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

    public Double getWaist() {
        Double outputsSumLength = 0.0;
        for (CHOutput chOutput : patternDefinition) {
            Double outputSumLength = chOutput.getLength() - chOutput.getRelax();
            outputsSumLength += chOutput.getCount() * outputSumLength;
        }
        return this.input.getLength() - outputsSumLength;
    }

    public List<PlanOutput> getSerialisableRelaxPatternDefinition(boolean relaxEnabled, boolean forceIntegerRelax) {
        List<PlanOutput> serialisablePatternDefinition = new ArrayList<PlanOutput>();
        for (CHOutput chOutput : patternDefinition) {
            if (!relaxEnabled) serialisablePatternDefinition.add(chOutput.getPlanOutput());
            else if (forceIntegerRelax) serialisablePatternDefinition.add(chOutput.getPlanOutputInteger());
            else serialisablePatternDefinition.add(chOutput.getPlanOutputDouble());
        }
        return serialisablePatternDefinition;
    }

}
