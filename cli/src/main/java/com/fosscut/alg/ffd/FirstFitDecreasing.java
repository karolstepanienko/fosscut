package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class FirstFitDecreasing extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(FirstFitDecreasing.class);

    private Order sortedOrder;

    public FirstFitDecreasing(Order sortedOrder) {
        this.sortedOrder = sortedOrder;
    }

    public CuttingPlan getCuttingPlan() {
        return getCuttingPlan(sortedOrder);
    }

    public void run() {
        logger.info("");
        logger.info("Running cutting plan generation using a first-fit-decreasing algorithm...");
        sortedOrder.reverseSortOutputs();
        initSortedOrderDemands();
        setCuttingPlanPatterns(demandLoop());
    }

    private void initSortedOrderDemands() {
        List<Integer> sortedOrderDemands = new ArrayList<Integer>();
        for (OrderOutput output : sortedOrder.getOutputs()) {
            sortedOrderDemands.add(output.getCount());
        }
        setOrderDemands(sortedOrderDemands);
    }

    @Override
    protected List<CHPattern> generatePatternForEachInput() {
        List<CHPattern> ffdPatterns = new ArrayList<CHPattern>();
        for (OrderInput input : sortedOrder.getInputs()) {
            CHPattern ffdPattern = new CHPattern();
            ffdPattern.setInput(new OrderInput(input));

            List<CHOutput> ffdPatternDefinition = new ArrayList<CHOutput>();

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
                        new CHOutput(
                            sortedOrder.getOutputId(output),
                            output.getLength(),
                            relaxedItemFit,
                            output.getMaxRelax()
                        )
                    );
                } else if (itemFit >= 1) {
                    remainingSpace -= itemFit * output.getLength();
                    ffdPatternDefinition.add(
                        new CHOutput(
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
            getOrderDemands().get(sortedOrder.getOutputId(output))
        );
    }

    private int getRelaxedItemFit(Integer remainingSpace, Integer relaxedLength, OrderOutput output) {
        return Math.min(
            remainingSpace / relaxedLength,
            getOrderDemands().get(sortedOrder.getOutputId(output))
        );
    }

}
