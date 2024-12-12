package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.type.cutting.order.OrderOutput;

public class CuttingPlan {
    private List<PlanInput> inputs;
    private List<OrderOutput> outputs;

    public List<PlanInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<PlanInput> inputs) {
        this.inputs = inputs;
    }

    public List<OrderOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OrderOutput> outputs) {
        this.outputs = outputs;
    }
}
