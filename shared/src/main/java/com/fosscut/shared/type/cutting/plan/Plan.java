package com.fosscut.shared.type.cutting.plan;

import java.util.List;

import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.util.save.YamlDumper;

public class Plan {
    private List<PlanInput> inputs;
    private List<OrderOutput> outputs;
    private Metadata metadata;

    public Plan() {
        this.metadata = new Metadata();
    }

    public Plan(List<PlanInput> inputs, List<OrderOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.metadata = new Metadata();
        this.metadata.calculateMetadata(inputs, outputs);
    }

    public Plan(
        List<PlanInput> inputs,
        List<OrderOutput> outputs,
        Long elapsedTimeMilliseconds
    ) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.metadata = new Metadata(elapsedTimeMilliseconds);
        this.metadata.calculateMetadata(inputs, outputs);
    }

    public List<PlanInput> getInputs() {
        return inputs;
    }

    public List<OrderOutput> getOutputs() {
        return outputs;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String toString() {
        return new YamlDumper().dump(this);
    }

}
