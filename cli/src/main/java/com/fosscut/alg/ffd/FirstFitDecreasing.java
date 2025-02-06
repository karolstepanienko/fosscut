package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class FirstFitDecreasing extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(FirstFitDecreasing.class);

    private Order orderSortedOutputs;
    private boolean relaxEnabled;
    private boolean forceIntegerRelax;

    public FirstFitDecreasing(Order order, boolean relaxEnabled, boolean forceIntegerRelax) {
        this.orderSortedOutputs = order;
        this.relaxEnabled = relaxEnabled;
        this.forceIntegerRelax = forceIntegerRelax;
    }

    public CuttingPlan getCuttingPlan() {
        return getCuttingPlan(orderSortedOutputs, relaxEnabled, forceIntegerRelax);
    }

    public void run() throws GeneratedPatternsCannotBeEmptyException {
        logger.info("");
        logger.info("Running cutting plan generation using a first-fit-decreasing algorithm...");

        initInputCounts(orderSortedOutputs);
        orderSortedOutputs.reverseSortOutputs();
        initOrderDemands(orderSortedOutputs);

        setCuttingPlanPatterns(demandLoop());
    }

    @Override
    protected List<CHPattern> generatePatternForEachInput() {
        List<CHPattern> chPatterns = new ArrayList<CHPattern>();
        for (Integer inputId = 0; inputId < orderSortedOutputs.getInputs().size(); ++inputId) {
            Integer inputCount = getInputCounts().get(inputId);
            if (inputCount == null || inputCount > 0) {
                chPatterns.add(getPattern(inputId, orderSortedOutputs.getInputs().get(inputId)));
            }
        }
        return chPatterns;
    }

    private CHPattern getPattern(Integer inputId, OrderInput input) {
        CHPattern chPattern = new CHPattern();
        chPattern.setInputId(inputId);
        chPattern.setInput(new OrderInput(input));
        chPattern.setPatternDefinition(getPatternDefinition(input));
        return chPattern;
    }

    private List<CHOutput> getPatternDefinition(OrderInput input) {
        List<CHOutput> chPatternDefinition = new ArrayList<CHOutput>();

        Integer remainingSpace = input.getLength();

        int i = 0;
        while (i < orderSortedOutputs.getOutputs().size() && remainingSpace > 0) {
            OrderOutput output = orderSortedOutputs.getOutputs().get(i);

            int itemFit = getItemFit(remainingSpace, output);

            Integer relaxedLength = null;
            Integer relaxedItemFit = null;
            if (output.getMaxRelax() != null){
                relaxedLength = output.getLength() - output.getMaxRelax();
                relaxedItemFit = getRelaxedItemFit(remainingSpace, relaxedLength, output);
            }

            if (relaxEnabled && relaxedLength != null
                && relaxedItemFit > itemFit && relaxedItemFit >= 1
            ) {
                remainingSpace -= relaxedItemFit * relaxedLength;
                chPatternDefinition.add(
                    new CHOutput(
                        orderSortedOutputs.getOutputId(output),
                        output.getLength(),
                        relaxedItemFit,
                        Double.valueOf(output.getMaxRelax())
                    )
                );
            } else if (itemFit >= 1) {
                remainingSpace -= itemFit * output.getLength();
                chPatternDefinition.add(
                    new CHOutput(
                        orderSortedOutputs.getOutputId(output),
                        output.getLength(),
                        itemFit,
                        0.0
                    )
                );
            }

            i += 1;
        }

        return chPatternDefinition;
    }

    private int getItemFit(Integer remainingSpace, OrderOutput output) {
        return Math.min(
            remainingSpace / output.getLength(),
            getOrderDemands().get(orderSortedOutputs.getOutputId(output))
        );
    }

    private int getRelaxedItemFit(Integer remainingSpace, Integer relaxedLength, OrderOutput output) {
        return Math.min(
            remainingSpace / relaxedLength,
            getOrderDemands().get(orderSortedOutputs.getOutputId(output))
        );
    }

}
