package com.fosscut.type.cutting.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.Element;
import com.fosscut.util.Messages;

public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private List<OrderInput> inputs;
    private List<OrderOutput> outputs;

    public Order() {}

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

    public Integer getInputsSumLength() {
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

    public void validate() {
        if (!longestInputLongerThanLongestOutput()) {
            logger.error(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR);
            System.exit(1);
        } else if (!lengthHasToBePositive(this.inputs)) {
            logger.error(Messages.NON_POSITIVE_INPUT_LENGTH_ERROR);
            System.exit(1);
        } else if (!lengthHasToBePositive(this.outputs)) {
            logger.error(Messages.NON_POSITIVE_OUTPUT_LENGTH_ERROR);
            System.exit(1);
        }
    }

    private boolean longestInputLongerThanLongestOutput() {
        OrderInput longestInput = Collections.max(this.inputs, Comparator.comparing(i -> i.getLength()));
        OrderOutput longestOutput = Collections.max(this.outputs, Comparator.comparing(i -> i.getLength()));
        return longestInput.getLength() >= longestOutput.getLength();
    }

    private boolean lengthHasToBePositive(List<? extends Element> elements) {
        boolean valid = true;
        for (Element element: elements) {
            if (element.getLength() <= 0) {
                valid = false;
                break;
            }
        }
        return valid;
    }

}
