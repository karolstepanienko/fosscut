package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.RelaxationSpread;
import com.fosscut.alg.SingleOutput;
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

    private RelaxationSpread relaxationSpread;

    public FFDPatternGenRelax(Order orderSortedOutputs,
        List<Integer> orderDemands,
        RelaxationSpreadStrategy relaxationSpreadStrategy
    ) {
        super(orderSortedOutputs, orderDemands);
        this.relaxationSpread = new RelaxationSpread(relaxationSpreadStrategy);
    }

    public List<CHOutput> getPatternDefinition(OrderInput input) {
        List<SingleOutput> singlePatternDefinition = new ArrayList<SingleOutput>();

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
                    singlePatternDefinition.add(new SingleOutput(
                        orderSortedOutputs.getOutputId(output),
                        output.getLength(),
                        output.getMaxRelax(),
                        output.getMaxRelax()
                    ));
                }
            }

            i += 1;
        }

        if (numberOfRelaxedOutputs > 0) {
            singlePatternDefinition = relaxationSpread.applyRelaxationSpread(
                singlePatternDefinition, remainingSpace, numberOfRelaxedOutputs);
        }

        return relaxationSpread.convertSingleToChPatternDefinition(singlePatternDefinition);
    }

}
