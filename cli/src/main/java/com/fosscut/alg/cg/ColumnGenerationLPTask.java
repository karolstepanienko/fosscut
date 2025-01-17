package com.fosscut.alg.cg;

import com.fosscut.alg.LPTask;
import com.fosscut.type.cutting.order.Order;

public class ColumnGenerationLPTask extends LPTask {

    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
