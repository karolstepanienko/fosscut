package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.util.save.YamlDumper;

public class CuttingPlan {
    private List<PlanInput> inputs;
    private List<OrderOutput> outputs;
    private Integer totalNeededInputLength;
    private Double totalCost;

    public CuttingPlan(List<PlanInput> inputs, List<OrderOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        calculateTotalNeededInputLength();
        calculateTotalCost();
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

    public Double getTotalCost() {
        return totalCost;
    }

    private void calculateTotalNeededInputLength() {
        totalNeededInputLength = 0;
        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                totalNeededInputLength += input.getLength() * pattern.getCount();
            }
        }
    }

    private void calculateTotalCost() {
        totalCost = 0.0;
        for (PlanInput input : inputs) {
            Double inputCost = input.getCost();
            if (inputCost != null) {
                for (Pattern pattern: input.getPatterns()) {
                    totalCost += inputCost * pattern.getCount();
                }
            } else {
                totalCost = null;
                return;
            }
        }
    }

    @Override
    public String toString() {
        YamlDumper yamlDumper = new YamlDumper();
        return yamlDumper.dump(this);
    }

}
