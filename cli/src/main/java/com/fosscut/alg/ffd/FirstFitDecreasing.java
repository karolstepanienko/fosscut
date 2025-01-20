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
    private boolean relaxEnabled;
    private boolean forceIntegerRelax;

    public FirstFitDecreasing(Order sortedOrder, boolean relaxEnabled, boolean forceIntegerRelax) {
        this.sortedOrder = sortedOrder;
        this.relaxEnabled = relaxEnabled;
        this.forceIntegerRelax = forceIntegerRelax;
    }

    public CuttingPlan getCuttingPlan() {
        return getCuttingPlan(sortedOrder, relaxEnabled, forceIntegerRelax);
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
        List<CHPattern> chPatterns = new ArrayList<CHPattern>();
        for (OrderInput input : sortedOrder.getInputs()) {
            CHPattern chPattern = new CHPattern();
            chPattern.setInput(new OrderInput(input));

            List<CHOutput> chPatternDefinition = new ArrayList<CHOutput>();

            Integer remainingSpace = input.getLength();

            int i = 0;
            while (i < sortedOrder.getOutputs().size() && remainingSpace > 0) {
                OrderOutput output = sortedOrder.getOutputs().get(i);

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
                            sortedOrder.getOutputId(output),
                            output.getLength(),
                            relaxedItemFit,
                            Double.valueOf(output.getMaxRelax())
                        )
                    );
                } else if (itemFit >= 1) {
                    remainingSpace -= itemFit * output.getLength();
                    chPatternDefinition.add(
                        new CHOutput(
                            sortedOrder.getOutputId(output),
                            output.getLength(),
                            itemFit,
                            0.0
                        )
                    );
                }

                i += 1;
            }

            chPattern.setPatternDefinition(chPatternDefinition);
            chPatterns.add(chPattern);
        }

        return chPatterns;
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
