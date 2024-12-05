package com.fosscut.type;

public abstract class Element {
    private Integer length;

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String toString() {
        return "Element: length = " + this.getLength();
    }
}
