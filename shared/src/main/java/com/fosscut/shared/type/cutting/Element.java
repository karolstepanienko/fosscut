package com.fosscut.shared.type.cutting;

public abstract class Element implements Comparable<Element> {

    private Integer length;

    public Element() {}

    public Element(Integer length) {
        this.length = length;
    }

    public Element(Element element) {
        this.length = element.getLength();
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String toString() {
        return "Element: length = " + this.getLength();
    }

    public int compareTo(Element element) {
        return this.length - element.length;
    }

}
