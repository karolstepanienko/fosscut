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
        CHCuttingPlanFormatter chCuttingPlanFormatter = new CHCuttingPlanFormatter(order);
        return chCuttingPlanFormatter.getCuttingPlan(cuttingPlanPatterns);
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
        for (CHOutput chOutput : minWastePattern.getPatternDefinition()) {
            if (chOutput.getCount() > 0) {

                Integer maxPossiblePatternCount =
                    orderDemands.get(chOutput.getId())
                    / chOutput.getCount();

                patternCount = Math.min(patternCount, maxPossiblePatternCount);
            }
        }
        minWastePattern.setCount(patternCount);
    }

    protected void decreaseOrderOutputCount(CHPattern minWastePattern) {
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
