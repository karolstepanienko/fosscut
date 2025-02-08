package com.fosscut.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;

public class Cleaner {

    private static final Logger logger = LoggerFactory.getLogger(Cleaner.class);

    public void cleanOrder(Order order) {
        logger.info("Removing unnecessary order elements...");
        clean(order);
        logger.info("Cleanup done.");
    }

    /*
     * Removes any OrderInput with a 'count: 0'. Those are not used during plan
     * generation.
     */
    private void clean(Order order) {
        List<OrderInput> inputs = new ArrayList<OrderInput>();
        for (OrderInput input : order.getInputs()) {
            if (input.getCount() == null || input.getCount() > 0) {
                inputs.add(input);
            }
        }
        order.setInputs(inputs);
    }

}
