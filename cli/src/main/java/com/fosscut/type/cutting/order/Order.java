package com.fosscut.type.cutting.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.Element;
import com.fosscut.util.Messages;
import com.fosscut.util.save.YamlDumper;

public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

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
        } else if (!sumInputLengthLongerThanSumOutputLength()) {
            logger.error(Messages.OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR);
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

    private boolean sumInputLengthLongerThanSumOutputLength() {
        boolean sumInputLongerThanSumOutput = false;
        if (allInputCountsDefined())
            sumInputLongerThanSumOutput =
                calculateSumLength(inputs) >= calculateSumLength(outputs);
        else sumInputLongerThanSumOutput = true;
        return sumInputLongerThanSumOutput;
    }

    private boolean allInputCountsDefined() {
        for (OrderInput input : this.inputs) {
            if (input.getCount() == null) {
                return false;
            }
         }
         return true;
    }

    private Integer calculateSumLength(List<? extends OrderElement> elements) {
        Integer sumLength = 0;
        for (OrderElement element: elements) {
            sumLength += element.getCount() * element.getLength();
        }
        return sumLength;
    }

}
