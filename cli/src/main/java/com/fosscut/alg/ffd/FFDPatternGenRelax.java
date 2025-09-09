package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.ffd.abs.AbstractFFDPatternGen;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHOutput;

/*
 * This class implements FFD pattern generation strategy that uses relaxation
 * to fill the input length more effectively.
 * Creates a pattern definition candidate that uses max relaxation for all
 * output elements. This ensures that pattern has as much output items as
 * possible. At the end when no other output element fits, tries to stretch
 * items to fill the remaining space.
 * Strategies to spread the remaining space: first output, equal, last output.
 */
public class FFDPatternGenRelax extends AbstractFFDPatternGen {

    private RelaxationSpreadStrategy relaxationSpreadStrategy;

    public FFDPatternGenRelax(Order orderSortedOutputs,
        List<Integer> orderDemands,
        RelaxationSpreadStrategy relaxationSpreadStrategy
    ) {
        super(orderSortedOutputs, orderDemands);
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
    }

    public List<CHOutput> getPatternDefinition(OrderInput input) {
        List<FFDOutput> ffdPatternDefinition = new ArrayList<FFDOutput>();

        int remainingSpace = input.getLength();
        int numberOfRelaxedOutputs = 0;

        int i = 0;
        while (i < orderSortedOutputs.getOutputs().size() && remainingSpace > 0) {
            OrderOutput output = orderSortedOutputs.getOutputs().get(i);

            int maxRelaxedLength = output.getLength();
            if (output.getMaxRelax() != null) {
                maxRelaxedLength -= output.getMaxRelax();
            }

            int relaxedItemFit = getItemFit(remainingSpace, maxRelaxedLength, output);

            if (output.getMaxRelax() != null && output.getMaxRelax() > 0) {
                numberOfRelaxedOutputs += relaxedItemFit;
            }

            if (relaxedItemFit >= 1) {
                remainingSpace -= relaxedItemFit * maxRelaxedLength;
                for (int j = 0; j < relaxedItemFit; ++j) {
                    ffdPatternDefinition.add(new FFDOutput(
                        orderSortedOutputs.getOutputId(output),
                        output.getLength(),
                        output.getMaxRelax()
                    ));
                }
            }

            i += 1;
        }

        if (numberOfRelaxedOutputs > 0) {
            if (relaxationSpreadStrategy == RelaxationSpreadStrategy.EQUAL) {
                // Spread remaining space equally to all items with available relaxation
                ffdPatternDefinition = equalSpreadRemainingSpace(ffdPatternDefinition, remainingSpace, numberOfRelaxedOutputs);
            } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.LONGEST) {
                // Spread remaining space to items on the end of the pattern (relax longest items first)
                ffdPatternDefinition = sideSpreadRemainingSpace(ffdPatternDefinition.reversed(), remainingSpace).reversed();
            } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.SHORTEST) {
                // Spread remaining space to items on the start of the pattern (relax shortest items first)
                ffdPatternDefinition = sideSpreadRemainingSpace(ffdPatternDefinition, remainingSpace);
            }
        }

        return convertFFDToChPatternDefinition(ffdPatternDefinition);
    }

    private List<FFDOutput> sideSpreadRemainingSpace(List<FFDOutput> ffdPatternDefinition, int remainingSpace) {
        int i = 0;
        while (remainingSpace > 0 && i < ffdPatternDefinition.size()) {
            FFDOutput ffdOutput = ffdPatternDefinition.get(i);
            Integer spreadAmount = Math.min(remainingSpace, ffdOutput.getRelax());
            ffdOutput.setRelax(ffdOutput.getRelax() - spreadAmount);
            remainingSpace -= spreadAmount;
            i++;
        }

        return ffdPatternDefinition;
    }

    private List<FFDOutput> equalSpreadRemainingSpace(List<FFDOutput> ffdPatternDefinition, int remainingSpace, int numberOfRelaxedOutputs) {
        int equalShare = Math.max(remainingSpace / numberOfRelaxedOutputs, 1);

        // avoids an infinite loop when remainingSpace will never reach 0
        int remainingRelax = getRemainingRelax(ffdPatternDefinition);

        int i = 0;
        while (remainingSpace > 0 && remainingRelax > 0) {
            FFDOutput ffdOutput = ffdPatternDefinition.get(i % ffdPatternDefinition.size());
            if (ffdOutput.getRelax() >= equalShare) {
                ffdOutput.setRelax(ffdOutput.getRelax() - equalShare);
                remainingSpace -= equalShare;
                remainingRelax -= equalShare;
            } else if (ffdOutput.getRelax() > 0) {
                // necessary to make sure that all remaining space is used
                ffdOutput.setRelax(ffdOutput.getRelax() - 1);
                remainingSpace -= 1;
                remainingRelax -= 1;
            }
            i++;
        }

        return ffdPatternDefinition;
    }

    private int getRemainingRelax(List<FFDOutput> ffdPatternDefinition) {
        int remainingRelax = 0;
        for (FFDOutput ffdOutput : ffdPatternDefinition) {
            remainingRelax += ffdOutput.getRelax();
        }
        return remainingRelax;
    }

    private List<CHOutput> convertFFDToChPatternDefinition(List<FFDOutput> ffdPatternDefinition) {
        List<CHOutput> chPatternDefinition = new ArrayList<CHOutput>();

        CHOutput latest = null;
        for (FFDOutput ffdOutput : ffdPatternDefinition) {
            if (latest != null
                && ffdOutput.getId().equals(latest.getId())
                && ffdOutput.getRelax().equals(latest.getRelax().intValue())) {
                latest.setCount(latest.getCount() + 1);
            } else {
                chPatternDefinition.add(new CHOutput(
                    ffdOutput.getId(),
                    ffdOutput.getLength(),
                    1,
                    Double.valueOf(ffdOutput.getRelax())
                ));
            }
            latest = chPatternDefinition.getLast();
        }

        return chPatternDefinition;
    }

}
