package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderOutput extends Element {

    private Integer count;
    private Integer maxRelax;

    public OrderOutput() {}

    public OrderOutput(OrderOutput orderOutput) {
        this.setLength(orderOutput.getLength());
        this.count = orderOutput.getCount();
        this.maxRelax = orderOutput.getMaxRelax();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
