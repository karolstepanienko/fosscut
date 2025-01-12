package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.ffd.FFDOutput;
import com.fosscut.type.cutting.ffd.FFDPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;

import com.fosscut.util.save.YamlDumper;

public class FirstFitDecreasing {

    private static final Logger logger = LoggerFactory.getLogger(YamlDumper.class);

    private Order sortedOrder;
    private List<Integer> sortedOrderDemands;

    public FirstFitDecreasing(Order order) {
        this.sortedOrder = order;
    }

    public void run() {
        sortedOrder.reverseSortOutputs();
        initSortedOrderDemands();
        demandLoop();
    }

    private void initSortedOrderDemands() {
        sortedOrderDemands = new ArrayList<Integer>();
        for (OrderOutput output : sortedOrder.getOutputs()) {
            sortedOrderDemands.add(output.getCount());
        }
    }

    private void demandLoop() {
        List<FFDPattern> cuttingPlanPatterns = new ArrayList<FFDPattern>();
        while (!isDemandSatisfied()) {
            List<FFDPattern> patternsForEachInput = generatePatterns();
            // debugGeneratedPatterns(patternsForEachInput);
            FFDPattern minWastePattern = getMinWastePattern(patternsForEachInput);
            calculateMinWastePatternCount(minWastePattern);
            decreaseOrderOutputCount(minWastePattern);
            cuttingPlanPatterns.add(minWastePattern);
        }
    }

    private boolean isDemandSatisfied() {
        boolean demandSatisfied = true;
        for (Integer demand : sortedOrderDemands) {
            if (demand > 0) {
                demandSatisfied = false;
                break;
            }
        }
        return demandSatisfied;
    }

    /**
     * Generates exactly one pattern for each input element
     */
    private List<FFDPattern> generatePatterns() {
        List<FFDPattern> ffdPatterns = new ArrayList<FFDPattern>();
        for (OrderInput input : sortedOrder.getInputs()) {
            FFDPattern ffdPattern = new FFDPattern();
            ffdPattern.setInput(new OrderInput(input));

            List<FFDOutput> ffdPatternDefinition = new ArrayList<FFDOutput>();

            Integer remainingSpace = input.getLength();

            int i = 0;
            while (i < sortedOrder.getOutputs().size() && remainingSpace > 0) {
                OrderOutput output = sortedOrder.getOutputs().get(i);

                int itemFit = getItemFit(remainingSpace, output);

                Integer relaxedLength = output.getLength() - output.getMaxRelax();
                int relaxedItemFit = getRelaxedItemFit(remainingSpace, relaxedLength, output);

                if (relaxedItemFit > itemFit && relaxedItemFit >= 1) {
                    remainingSpace -= relaxedItemFit * relaxedLength;

                    ffdPatternDefinition.add(
                        new FFDOutput(
                            sortedOrder.getOutputId(output),
                            output.getLength(),
                            relaxedItemFit,
                            output.getMaxRelax()
                        )
                    );
                } else if (itemFit >= 1) {
                    remainingSpace -= itemFit * output.getLength();
                    ffdPatternDefinition.add(
                        new FFDOutput(
                            sortedOrder.getOutputId(output),
                            output.getLength(),
                            itemFit,
                            0
                        )
                    );
                }

                i += 1;
            }

            ffdPattern.setPatternDefinition(ffdPatternDefinition);
            ffdPatterns.add(ffdPattern);
        }

        return ffdPatterns;
    }

    private int getItemFit(Integer remainingSpace, OrderOutput output) {
        return Math.min(
            remainingSpace / output.getLength(),
            sortedOrderDemands.get(sortedOrder.getOutputId(output))
        );
    }

    private int getRelaxedItemFit(Integer remainingSpace, Integer relaxedLength, OrderOutput output) {
        return Math.min(
            remainingSpace / relaxedLength,
            sortedOrderDemands.get(sortedOrder.getOutputId(output))
        );
    }

    private FFDPattern getMinWastePattern(List<FFDPattern> patterns) {
        FFDPattern minWastePattern = patterns.get(0);
        Integer minWaist = patterns.get(0).getWaist();
        for (FFDPattern pattern : patterns) {
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
    private void calculateMinWastePatternCount(FFDPattern minWastePattern) {
        Integer patternCount = Integer.MAX_VALUE;
        for (FFDOutput ffdOutput : minWastePattern.getPatternDefinition()) {
            if (ffdOutput.getCount() > 0) {

                Integer maxPossiblePatternCount =
                    sortedOrderDemands.get(ffdOutput.getId())
                    / ffdOutput.getCount();

                patternCount = Math.min(patternCount, maxPossiblePatternCount);
            }
        }
        minWastePattern.setCount(patternCount);
    }

    private void decreaseOrderOutputCount(FFDPattern minWastePattern) {
        for (FFDOutput ffdOutput : minWastePattern.getPatternDefinition()) {
            Integer demand = sortedOrderDemands.get(ffdOutput.getId());
            demand -= minWastePattern.getCount() * ffdOutput.getCount();
            sortedOrderDemands.set(ffdOutput.getId(), demand);
        }
    }

    private void debugGeneratedPatterns(List<FFDPattern> patterns) {
        for (FFDPattern pattern : patterns) {
            logger.info("Pattern input length:" + pattern.getInput().getLength());
            for (FFDOutput ffdOutput : pattern.getPatternDefinition()) {
                logger.info("output id: " + ffdOutput.getId());
                logger.info("output length: " + ffdOutput.getLength());
                logger.info("output length: " + sortedOrder.getOutputs().get(ffdOutput.getId()).getLength());
                logger.info("output count: " + ffdOutput.getCount());
                logger.info("output relax: " + ffdOutput.getRelax());
            }
            logger.info("Pattern waist: " + pattern.getWaist().toString());
        }
    }

}
