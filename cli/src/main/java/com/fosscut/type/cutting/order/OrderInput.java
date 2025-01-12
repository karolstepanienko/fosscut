package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderInput extends Element {

    public OrderInput() {}

    public OrderInput(OrderInput orderInput) {
        this.setLength(orderInput.getLength());
    }

    public String toString() {
        return "Input: length = " + this.getLength();
    }
}
