package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.ffd.abs.AbstractFFDPatternGen;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.CHOutput;

/*
 * This class implements FFD pattern generation without relaxation.
 */
public class FFDPatternGen extends AbstractFFDPatternGen {

    public FFDPatternGen(Order orderSortedOutputs, List<Integer> orderDemands) {
        super(orderSortedOutputs, orderDemands);
    }

    public List<CHOutput> getPatternDefinition(OrderInput input) {
        List<CHOutput> chPatternDefinition = new ArrayList<CHOutput>();

        int remainingSpace = input.getLength();

        int i = 0;
        while (i < orderSortedOutputs.getOutputs().size() && remainingSpace > 0) {
            OrderOutput output = orderSortedOutputs.getOutputs().get(i);

            int itemFit = getItemFit(remainingSpace, output.getLength(), output);

            if (itemFit >= 1) {
                remainingSpace -= itemFit * output.getLength();
                chPatternDefinition.add(
                    new CHOutput(
                        orderSortedOutputs.getOutputId(output),
                        output.getLength(),
                        itemFit,
                        null
                    )
                );
            }

            i += 1;
        }

        return chPatternDefinition;
    }

}
