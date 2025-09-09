package com.fosscut.alg.ffd.abs;

import java.util.List;

import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderOutput;

public abstract class AbstractFFDPatternGen {

    protected Order orderSortedOutputs;
    protected List<Integer> orderDemands;

    protected AbstractFFDPatternGen(Order orderSortedOutputs,
        List<Integer> orderDemands
    ) {
        this.orderSortedOutputs = orderSortedOutputs;
        this.orderDemands = orderDemands;
    }

    protected int getItemFit(int remainingSpace, Integer length, OrderOutput output) {
        return Math.min(
            remainingSpace / length,
            orderDemands.get(orderSortedOutputs.getOutputId(output))
        );
    }

}
