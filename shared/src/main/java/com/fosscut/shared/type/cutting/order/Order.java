package com.fosscut.shared.type.cutting.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fosscut.shared.util.save.YamlDumper;

public class Order {

    private List<OrderInput> inputs;
    private List<OrderOutput> outputs;

    public Order() {}

    public Order(List<OrderInput> inputs, List<OrderOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Order(Order order) {
        this.inputs = new ArrayList<OrderInput>();
        for (OrderInput input: order.getInputs()) {
            this.inputs.add(new OrderInput(input));
        }

        this.outputs = new ArrayList<OrderOutput>();
        for (OrderOutput output: order.getOutputs()) {
            this.outputs.add(new OrderOutput(output));
        }
    }

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

    @Override
    public String toString() {
        YamlDumper yamlDumper = new YamlDumper();
        return yamlDumper.dump(this);
    }

    public Integer getOutputId(OrderOutput orderOutput) {
        Integer outputId = -1;
        for (int i = 0; i < this.outputs.size(); i++) {
            if (this.outputs.get(i).getLength() == orderOutput.getLength()
                && this.outputs.get(i).getMaxRelax() == orderOutput.getMaxRelax()) {
                    outputId = i;
                    break;
                }
        }
        return outputId;
    }

    public Integer calculateInputsSumLength() {
        Integer sumLength = 0;
        for (OrderInput input : this.inputs) {
            sumLength += input.getLength();
        }
        return sumLength;
    }

    public void reverseSortOutputs() {
        Collections.sort(outputs);
        Collections.reverse(outputs);
    }

}
