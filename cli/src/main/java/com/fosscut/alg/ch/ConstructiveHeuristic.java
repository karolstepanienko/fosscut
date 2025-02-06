package com.fosscut.alg.ch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.plan.CuttingPlan;

public abstract class ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(ConstructiveHeuristic.class);

    private List<Integer> inputCounts;
    private List<Integer> orderDemands;
    private List<CHPattern> cuttingPlanPatterns;

    protected CuttingPlan getCuttingPlan(Order order, boolean relaxEnabled, boolean forceIntegerRelax) {
        CHCuttingPlanFormatter chCuttingPlanFormatter = new CHCuttingPlanFormatter(order, relaxEnabled, forceIntegerRelax);
        return chCuttingPlanFormatter.getCuttingPlan(cuttingPlanPatterns);
    }

    protected List<Integer> getInputCounts() {
        return inputCounts;
    }

    protected void setInputCounts(List<Integer> inputCounts) {
        this.inputCounts = inputCounts;
    }

    protected List<Integer> getOrderDemands() {
        return orderDemands;
    }

    protected void setOrderDemands(List<Integer> orderDemands) {
        this.orderDemands = orderDemands;
    }

    protected void setCuttingPlanPatterns(List<CHPattern> cuttingPlanPatterns) {
        this.cuttingPlanPatterns = cuttingPlanPatterns;
    }

    protected void initInputCounts(Order order) {
        List<Integer> inputCounts = new ArrayList<Integer>();
        for (OrderInput input : order.getInputs()) {
            inputCounts.add(input.getCount());
        }
        setInputCounts(inputCounts);
    }

    protected void initOrderDemands(Order order) {
        List<Integer> orderDemands = new ArrayList<Integer>();
        for (OrderOutput output : order.getOutputs()) {
            orderDemands.add(output.getCount());
        }
        setOrderDemands(orderDemands);
    }

    protected List<CHPattern> generatePatternForEachInput() {
        logger.error("Method generatePatternForEachInput() needs to be overridden.");
        System.exit(1);
        return null;
    }

    protected List<CHPattern> demandLoop() throws GeneratedPatternsCannotBeEmptyException {
        List<CHPattern> cuttingPlanPatterns = new ArrayList<CHPattern>();
        while (!isDemandSatisfied()) {
            List<CHPattern> patternsForEachInput = generatePatternForEachInput();

            if (patternsForEachInput.size() <= 0)
                throw new GeneratedPatternsCannotBeEmptyException("");

            // debugGeneratedPatterns(patternsForEachInput);
            CHPattern minWastePattern = getMinWastePattern(patternsForEachInput);
            calculateMinWastePatternCount(minWastePattern);
            decreaseOrderInputCount(minWastePattern);
            decreaseOrderOutputCount(minWastePattern);

            cuttingPlanPatterns.add(minWastePattern);
            logger.info("Input count: " + inputCounts);
            logger.info("Order demands: " + orderDemands);
        }
        return cuttingPlanPatterns;
    }

    private boolean isDemandSatisfied() {
        boolean demandSatisfied = true;
        for (Integer demand : orderDemands) {
            if (demand > 0) {
                demandSatisfied = false;
                break;
            }
        }
        return demandSatisfied;
    }

    private CHPattern getMinWastePattern(List<CHPattern> patterns) {
        CHPattern minWastePattern = patterns.get(0);
        Double minWaist = patterns.get(0).getWaist();
        for (CHPattern pattern : patterns) {
            Double waist = pattern.getWaist();
            if (waist < minWaist) {
                minWaist = waist;
                minWastePattern = pattern;
            }
        }
        return minWastePattern;
    }

    private void calculateMinWastePatternCount(CHPattern minWastePattern) {
        Integer patternCountFromInput = inputCounts.get(minWastePattern.getInputId());
        Integer patternCountFromOutputDemand = getMinWastePatternCountFromOutputDemand(minWastePattern);
        if (patternCountFromInput == null)
            minWastePattern.setCount(patternCountFromOutputDemand);
        else
            minWastePattern.setCount(
                Math.min(patternCountFromInput, patternCountFromOutputDemand)
            );
    }

    private Integer getMinWastePatternCountFromOutputDemand(CHPattern minWastePattern) {
        Integer patternCount = Integer.MAX_VALUE;

        for (CHOutput chOutput : minWastePattern.getPatternDefinition()) {
            if (chOutput.getCount() > 0) {

                Integer maxPossiblePatternCount =
                    orderDemands.get(chOutput.getId())
                    / chOutput.getCount();

                patternCount = Math.min(patternCount, maxPossiblePatternCount);
            }
        }

        return patternCount;
    }

    private void decreaseOrderInputCount(CHPattern minWastePattern) {
        Integer inputCount = inputCounts.get(minWastePattern.getInputId());
        if (inputCount != null) {
            inputCount -= minWastePattern.getCount();
            inputCounts.set(minWastePattern.getInputId(), inputCount);
        }
    }

    private void decreaseOrderOutputCount(CHPattern minWastePattern) {
        for (CHOutput chOutput : minWastePattern.getPatternDefinition()) {
            Integer demand = orderDemands.get(chOutput.getId());
            demand -= minWastePattern.getCount() * chOutput.getCount();
            orderDemands.set(chOutput.getId(), demand);
        }
    }

    protected void debugGeneratedPatterns(List<CHPattern> patterns) {
        for (CHPattern pattern : patterns) {
            logger.info("Pattern input length:" + pattern.getInput().getLength());
            for (CHOutput chOutput : pattern.getPatternDefinition()) {
                logger.info("output id: " + chOutput.getId());
                logger.info("output length: " + chOutput.getLength());
                logger.info("output count: " + chOutput.getCount());
                logger.info("output relax: " + chOutput.getRelax());
            }
            logger.info("Pattern waist: " + pattern.getWaist().toString());
        }
    }

}
