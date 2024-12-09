package com.fosscut.utils;

import com.fosscut.type.cutting.order.Order;

public class Validator {
    private boolean quietModeRequested;

    public Validator(boolean quietModeRequested) {
        this.quietModeRequested = quietModeRequested;
    }

    public void validateOrder(Order order) {
        if(!quietModeRequested) System.out.println("Running order validation...");
        order.validate();
        if(!quietModeRequested) System.out.println("Order valid.");
    }
}
