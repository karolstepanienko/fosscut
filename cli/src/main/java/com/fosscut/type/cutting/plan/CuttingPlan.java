package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.type.cutting.order.OrderOutput;

public class CuttingPlan {
    private List<PlanInput> inputs;
    private List<OrderOutput> outputs;
    private Integer totalNeededInputLength;

    public CuttingPlan(List<PlanInput> inputs, List<OrderOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        calculateTotalNeededInputLength();
    }

    public List<PlanInput> getInputs() {
        return inputs;
    }

    public List<OrderOutput> getOutputs() {
        return outputs;
    }

    public Integer getTotalNeededInputLength() {
        return totalNeededInputLength;
    }

    private void calculateTotalNeededInputLength() {
        totalNeededInputLength = 0;
        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                totalNeededInputLength += input.getLength() * pattern.getCount();
            }
        }
    }

}
