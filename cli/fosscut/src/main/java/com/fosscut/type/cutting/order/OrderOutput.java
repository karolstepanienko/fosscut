package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderOutput extends Element {
    private Integer number;
    private Integer maxRelax;

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMaxRelax() {
        return this.maxRelax;
    }

    public void setMaxRelax(Integer maxRelax) {
        this.maxRelax = maxRelax;
    }

    public String toString() {
        return "Output: length = " + this.getLength()
            + ", number = " + this.getNumber()
            + ", maxRelax = " + this.getMaxRelax();
    }
}
