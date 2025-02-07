package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderElement extends Element {

    private Integer count;

    public OrderElement() {}

    public OrderElement(Integer length, Integer count) {
        super(length);
        this.count = count;
    }

    public OrderElement(OrderElement element) {
        super(element);
        this.count = element.getCount();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
