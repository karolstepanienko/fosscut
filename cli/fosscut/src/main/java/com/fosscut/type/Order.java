package com.fosscut.type;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Order {
    private List<Input> inputs;
    private List<Output> outputs;

    public List<Input> getInputs() {
        return this.inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public List<Output> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public boolean isValid() {
        Input longestInput = Collections.max(this.inputs, Comparator.comparing(i -> i.getLength()));
        Output longestOutput = Collections.max(this.outputs, Comparator.comparing(i -> i.getLength()));
        return longestInput.getLength() >= longestOutput.getLength();
    }

    public void validate() {
        if (!this.isValid()) {
            System.err.println("Order invalid. Longest input element must be longer than longest output element.");
            System.exit(1);
        }
    }
}
