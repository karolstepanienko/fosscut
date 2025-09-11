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

    public Integer getWaist() {
        return this.input.getLength() - getOutputsSumLength(true);
    }

    // Relaxation values are ignored. If pattern is able to fit more or longer
    // output elements thanks to relaxation then it should be preferred.
    public Double getOutputLengthUnitCost() {
        return this.input.getCost() / getOutputsSumLength(false);
    }

    public List<PlanOutput> getSerialisableRelaxPatternDefinition() {
        List<PlanOutput> serialisablePatternDefinition = new ArrayList<PlanOutput>();
        for (CHOutput chOutput : patternDefinition) {
            serialisablePatternDefinition.add(chOutput.getPlanOutput());
        }
        return serialisablePatternDefinition;
    }

    public Integer getOutputsSumCount() {
        Integer outputsSumCount = 0;
        for (CHOutput chOutput : patternDefinition) {
            outputsSumCount += chOutput.getCount();
        }
        return outputsSumCount;
    }

    private Integer getOutputsSumLength(boolean includeRelax) {
        Integer outputsSumLength = 0;
        for (CHOutput chOutput : patternDefinition) {
            Integer outputSumLength = chOutput.getLength();
            Integer relax = chOutput.getRelax();
            if (includeRelax && relax != null) outputSumLength -= relax;
            outputsSumLength += chOutput.getCount() * outputSumLength;
        }
        return outputsSumLength;
    }

}
