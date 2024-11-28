package com.fosscut.utils;

import com.fosscut.type.Order;

public class Validator {
    public void validateOrder(Order order) {
        System.out.println("Running order validation...");
        order.validate();
        System.out.println("Order valid.");
    }
}
