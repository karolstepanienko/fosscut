package com.fosscut.alg.ch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.plan.CuttingPlan;

public abstract class ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(ConstructiveHeuristic.class);

    protected OptimizationCriterion optimizationCriterion;
    protected boolean forceIntegerRelax;

    private List<Integer> inputCounts;
    private List<Integer> orderDemands;
    private List<CHPattern> cuttingPlanPatterns;

    protected ConstructiveHeuristic(OptimizationCriterion optimizationCriterion,
        boolean forceIntegerRelax
    ) {
        this.optimizationCriterion = optimizationCriterion;
        this.forceIntegerRelax = forceIntegerRelax;
    }

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

    protected abstract List<CHPattern> generatePatternForEachInput()
        throws LPUnfeasibleException;

    protected List<CHPattern> demandLoop()
        throws GeneratedPatternsCannotBeEmptyException, LPUnfeasibleException
    {
        List<CHPattern> cuttingPlanPatterns = new ArrayList<CHPattern>();
        while (!isDemandSatisfied()) {
            List<CHPattern> patternsForEachInput = generatePatternForEachInput();

            if (patternsForEachInput.size() <= 0)
                throw new GeneratedPatternsCannotBeEmptyException("");

            // debugGeneratedPatterns(patternsForEachInput);
            CHPattern bestPattern;
            if (optimizationCriterion == OptimizationCriterion.MIN_COST)
                bestPattern = getMostEfficientPattern(patternsForEachInput);
            else bestPattern = getMinWastePattern(patternsForEachInput);

            calculateBestPatternCount(bestPattern);
            decreaseOrderInputCount(bestPattern);
            decreaseOrderOutputCount(bestPattern);

            cuttingPlanPatterns.add(bestPattern);
            if (inputCountDefined()) logger.info("Input count: " + inputCounts);
            logger.info("Order demands: " + orderDemands);
        }
        return cuttingPlanPatterns;
    }

    private boolean inputCountDefined() {
        for (Integer inputCount : getInputCounts()) {
           if (inputCount != null && inputCount > 0) return true;
        }
        return false;
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

    private CHPattern getMostEfficientPattern(List<CHPattern> patterns) {
        CHPattern mostEfficientPattern = patterns.get(0);
        Double minCost = patterns.get(0).getOutputLengthUnitCost();
        for (CHPattern pattern : patterns) {
            Double cost = pattern.getOutputLengthUnitCost();
            if (cost < minCost) {
                minCost = cost;
                mostEfficientPattern = pattern;
            }
        }
        return mostEfficientPattern;
    }

    private void calculateBestPatternCount(CHPattern bestPattern) {
        Integer patternCountFromInput = inputCounts.get(bestPattern.getInputId());
        Integer patternCountFromOutputDemand = getBestPatternCountFromOutputDemand(bestPattern);
        if (patternCountFromInput == null)
            bestPattern.setCount(patternCountFromOutputDemand);
        else
            bestPattern.setCount(
                Math.min(patternCountFromInput, patternCountFromOutputDemand)
            );
    }

    private Integer getBestPatternCountFromOutputDemand(CHPattern besPattern) {
        Integer patternCount = Integer.MAX_VALUE;

        for (CHOutput chOutput : besPattern.getPatternDefinition()) {
            if (chOutput.getCount() > 0) {

                // Ignores the remainder, rounds down, eg. 16/9 = 1.(7) => 1
                Integer maxPossiblePatternCount =
                    orderDemands.get(chOutput.getId())
                    / chOutput.getCount();

                patternCount = Math.min(patternCount, maxPossiblePatternCount);
            }
        }

        return patternCount;
    }

    private void decreaseOrderInputCount(CHPattern bestPattern) {
        Integer inputCount = inputCounts.get(bestPattern.getInputId());
        if (inputCount != null) {
            inputCount -= bestPattern.getCount();
            inputCounts.set(bestPattern.getInputId(), inputCount);
        }
    }

    private void decreaseOrderOutputCount(CHPattern bestPattern) {
        for (CHOutput chOutput : bestPattern.getPatternDefinition()) {
            Integer demand = orderDemands.get(chOutput.getId());
            demand -= bestPattern.getCount() * chOutput.getCount();
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
