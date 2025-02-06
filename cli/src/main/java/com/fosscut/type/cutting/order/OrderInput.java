package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderInput extends Element {

    private Integer count;

    public OrderInput() {}

    public OrderInput(Integer length) {
        super(length);
    }

    public OrderInput(OrderInput orderInput) {
        this.setLength(orderInput.getLength());
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String toString() {
        return "Input: length = " + this.getLength();
    }

}
