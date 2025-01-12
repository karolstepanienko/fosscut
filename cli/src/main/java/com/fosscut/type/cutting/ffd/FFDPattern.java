package com.fosscut.type.cutting.ffd;

import java.util.List;

import com.fosscut.type.cutting.order.OrderInput;

public class FFDPattern {

    private OrderInput input;
    private Integer count;
    private List<FFDOutput> patternDefinition;

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

    public List<FFDOutput> getPatternDefinition() {
        return patternDefinition;
    }

    public void setPatternDefinition(List<FFDOutput> patternDefinition) {
        this.patternDefinition = patternDefinition;
    }

    public Integer getWaist() {
        Integer outputsSumLength = 0;
        for (FFDOutput ffdOutput : patternDefinition) {
            Integer outputSumLength = ffdOutput.getLength() - ffdOutput.getRelax();
            outputsSumLength += ffdOutput.getCount() * outputSumLength;
        }
        return this.input.getLength() - outputsSumLength;
    }
}
