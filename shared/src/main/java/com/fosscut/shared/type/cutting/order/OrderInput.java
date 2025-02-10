package com.fosscut.shared.type.cutting.order;

public class OrderInput extends OrderElement {

    public OrderInput() {}

    public OrderInput(Integer length) {
        super(length, null);
    }

    public OrderInput(Integer length, Integer count) {
        super(length, count);
    }

    public OrderInput(OrderInput orderInput) {
        this.setLength(orderInput.getLength());
        this.setCount(orderInput.getCount());
    }

    public String toString() {
        return "Input: length = " + this.getLength();
    }

}
