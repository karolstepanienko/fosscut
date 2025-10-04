package com.fosscut.shared.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.shared.exception.OrderValidationException;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.Element;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderElement;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;

public class OrderValidator {

    private static final Logger logger = LoggerFactory.getLogger(OrderValidator.class);

    private OptimizationCriterion optimizationCriterion;

    public OrderValidator() {}
    public OrderValidator(OptimizationCriterion optimizationCriterion) {
        this.optimizationCriterion = optimizationCriterion;
    }

    public void validateOrder(Order order) throws OrderValidationException {
        logger.info("Running order validation...");
        validate(order);
        logger.info(SharedMessages.ORDER_VALID);
    }

    private void validate(Order order) throws OrderValidationException {
        if (!lengthHasToBePositive(order.getInputs())) throw new OrderValidationException(SharedMessages.NONPOSITIVE_INPUT_LENGTH_ERROR);
        else if (!lengthHasToBePositive(order.getOutputs())) throw new OrderValidationException(SharedMessages.NONPOSITIVE_OUTPUT_LENGTH_ERROR);
        else if (!countHasToBePositive(order.getInputs())) throw new OrderValidationException(SharedMessages.NONNEGATIVE_INPUT_COUNT_ERROR);
        else if (!countHasToBePositive(order.getOutputs())) throw new OrderValidationException(SharedMessages.NONNEGATIVE_OUTPUT_COUNT_ERROR);
        else if (!longestInputLongerThanLongestOutput(order)) throw new OrderValidationException(SharedMessages.OUTPUT_LONGER_THAN_INPUT_ERROR);
        // TODO maxRelax <= length
        else if (!sumInputLengthLongerThanSumOutputLength(order)) throw new OrderValidationException(SharedMessages.OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR);
        else if (optimizationCriterion == OptimizationCriterion.MIN_COST && !allInputCostsDefined(order.getInputs())) throw new OrderValidationException(SharedMessages.NULL_COST_EXCEPTION);
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

    private boolean countHasToBePositive(List<? extends OrderElement> orderElements) {
        boolean valid = true;
        for (OrderElement element: orderElements) {
            if (element.getCount() != null && element.getCount() < 0) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean longestInputLongerThanLongestOutput(Order order) {
        OrderInput longestInput = Collections.max(order.getInputs(), Comparator.comparing(i -> i.getLength()));
        OrderOutput longestOutput = Collections.max(order.getOutputs(), Comparator.comparing(i -> i.getLength()));
        return longestInput.getLength() >= longestOutput.getLength();
    }

    private boolean sumInputLengthLongerThanSumOutputLength(Order order) {
        boolean sumInputLongerThanSumOutput = false;
        if (allInputCountsDefined(order.getInputs()))
            sumInputLongerThanSumOutput =
                calculateSumLength(order.getInputs())
                >= calculateSumLength(order.getOutputs());
        else sumInputLongerThanSumOutput = true;
        return sumInputLongerThanSumOutput;
    }

    private boolean allInputCountsDefined(List<OrderInput> inputs) {
        for (OrderInput input : inputs) {
            if (input.getCount() == null) {
                return false;
            }
         }
         return true;
    }

    private Integer calculateSumLength(List<? extends OrderElement> orderElements) {
        Integer sumLength = 0;
        for (OrderElement element: orderElements) {
            sumLength += element.getCount() * element.getLength();
        }
        return sumLength;
    }

    private boolean allInputCostsDefined(List<OrderInput> inputs) {
        for (OrderInput input : inputs) {
            if (input.getCost() == null) {
                return false;
            }
        }
        return true;
    }

}
