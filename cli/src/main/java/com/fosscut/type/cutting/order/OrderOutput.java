package com.fosscut.type.cutting.order;

public class OrderOutput extends OrderElement {

    private Integer maxRelax;

    public OrderOutput() {}

    public OrderOutput(Integer length, Integer count) {
        super(length, count);
    }

    public OrderOutput(Integer length, Integer count, Integer maxRelax) {
        super(length, count);
        this.maxRelax = maxRelax;
    }

    public OrderOutput(OrderOutput orderOutput) {
        this.setLength(orderOutput.getLength());
        this.setCount(orderOutput.getCount());
        this.maxRelax = orderOutput.getMaxRelax();
    }

    public Integer getMaxRelax() {
        return this.maxRelax;
    }

    public void setMaxRelax(Integer maxRelax) {
        this.maxRelax = maxRelax;
    }

    public String toString() {
        return "Output: length = " + this.getLength()
            + ", count = " + this.getCount()
            + ", maxRelax = " + this.getMaxRelax();
    }

}
