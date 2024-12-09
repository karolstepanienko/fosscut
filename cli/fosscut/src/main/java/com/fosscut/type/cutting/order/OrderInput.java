package com.fosscut.type.cutting.order;

import com.fosscut.type.cutting.Element;

public class OrderInput extends Element {
    public String toString() {
        return "Input: length = " + this.getLength();
    }
}
