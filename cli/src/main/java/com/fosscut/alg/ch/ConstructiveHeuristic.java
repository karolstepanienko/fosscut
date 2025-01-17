package com.fosscut.alg.ch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.plan.CuttingPlan;

public abstract class ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(ConstructiveHeuristic.class);

    private List<Integer> orderDemands;
    List<CHPattern> cuttingPlanPatterns;

    protected CuttingPlan getCuttingPlan(Order order) {
        CHCuttingPlanFormatter ffdCuttingPlanFormatter = new CHCuttingPlanFormatter(order);
        return ffdCuttingPlanFormatter.getCuttingPlan(cuttingPlanPatterns);
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

    protected List<CHPattern> generatePatternForEachInput() {
        logger.error("Method generatePatternForEachInput() needs to be overridden.");
        System.exit(1);
        return null;
    }

    protected List<CHPattern> demandLoop() {
        List<CHPattern> cuttingPlanPatterns = new ArrayList<CHPattern>();
        while (!isDemandSatisfied()) {
            List<CHPattern> patternsForEachInput = generatePatternForEachInput();
            // debugGeneratedPatterns(patternsForEachInput);
            CHPattern minWastePattern = getMinWastePattern(patternsForEachInput);
            calculateMinWastePatternCount(minWastePattern);
            decreaseOrderOutputCount(minWastePattern);
            cuttingPlanPatterns.add(minWastePattern);
            logger.info("Order demands: " + orderDemands);
        }
        return cuttingPlanPatterns;
    }

    protected boolean isDemandSatisfied() {
        boolean demandSatisfied = true;
        for (Integer demand : orderDemands) {
            if (demand > 0) {
                demandSatisfied = false;
                break;
            }
        }
        return demandSatisfied;
    }

    protected CHPattern getMinWastePattern(List<CHPattern> patterns) {
        CHPattern minWastePattern = patterns.get(0);
        Integer minWaist = patterns.get(0).getWaist();
        for (CHPattern pattern : patterns) {
            Integer waist = pattern.getWaist();
            if (waist < minWaist) {
                minWaist = waist;
                minWastePattern = pattern;
            }
        }
        return minWastePattern;
    }

    /*
     * Calculates how many times this pattern could be used
     */
    protected void calculateMinWastePatternCount(CHPattern minWastePattern) {
        Integer patternCount = Integer.MAX_VALUE;
        for (CHOutput ffdOutput : minWastePattern.getPatternDefinition()) {
            if (ffdOutput.getCount() > 0) {

                Integer maxPossiblePatternCount =
                    orderDemands.get(ffdOutput.getId())
                    / ffdOutput.getCount();

                patternCount = Math.min(patternCount, maxPossiblePatternCount);
            }
        }
        minWastePattern.setCount(patternCount);
    }

    protected void decreaseOrderOutputCount(CHPattern minWastePattern) {
        for (CHOutput ffdOutput : minWastePattern.getPatternDefinition()) {
            Integer demand = orderDemands.get(ffdOutput.getId());
            demand -= minWastePattern.getCount() * ffdOutput.getCount();
            orderDemands.set(ffdOutput.getId(), demand);
        }
    }

    protected void debugGeneratedPatterns(List<CHPattern> patterns) {
        for (CHPattern pattern : patterns) {
            logger.info("Pattern input length:" + pattern.getInput().getLength());
            for (CHOutput ffdOutput : pattern.getPatternDefinition()) {
                logger.info("output id: " + ffdOutput.getId());
                logger.info("output length: " + ffdOutput.getLength());
                logger.info("output count: " + ffdOutput.getCount());
                logger.info("output relax: " + ffdOutput.getRelax());
            }
            logger.info("Pattern waist: " + pattern.getWaist().toString());
        }
    }

}
