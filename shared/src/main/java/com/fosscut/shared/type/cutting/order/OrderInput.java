package com.fosscut.shared.type.cutting.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInput extends OrderElement {

    private Double cost;

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
        this.setCost(orderInput.getCost());
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String toString() {
        return "Input: length = " + this.getLength();
    }

}
