package com.fosscut.type.cutting.order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Order {
    private List<OrderInput> inputs;
    private List<OrderOutput> outputs;

    public List<OrderInput> getInputs() {
        return this.inputs;
    }

    public void setInputs(List<OrderInput> inputs) {
        this.inputs = inputs;
    }

    public List<OrderOutput> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(List<OrderOutput> outputs) {
        this.outputs = outputs;
    }

    public Integer getInputsSumLength() {
        Integer sumLength = 0;
        for (OrderInput input : this.inputs) {
            sumLength += input.getLength();
        }
        return sumLength;
    }

    public boolean isValid() {
        OrderInput longestInput = Collections.max(this.inputs, Comparator.comparing(i -> i.getLength()));
        OrderOutput longestOutput = Collections.max(this.outputs, Comparator.comparing(i -> i.getLength()));
        return longestInput.getLength() >= longestOutput.getLength();
    }

    public void validate() {
        if (!this.isValid()) {
            System.err.println("Order invalid. Longest input element must be longer than longest output element.");
            System.exit(1);
        }
    }
}
