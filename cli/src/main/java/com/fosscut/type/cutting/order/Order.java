package com.fosscut.type.cutting.order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fosscut.type.cutting.Element;
import com.fosscut.util.Messages;

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

    public void validate() {
        if (!longestInputLongerThanLongestOutput()) {
            System.err.println(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR);
            System.exit(1);
        } else if (!lengthHasToBePositive(this.inputs)) {
            System.err.println(Messages.NON_POSITIVE_INPUT_LENGTH_ERROR);
            System.exit(1);
        } else if (!lengthHasToBePositive(this.outputs)) {
            System.err.println(Messages.NON_POSITIVE_OUTPUT_LENGTH_ERROR);
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
