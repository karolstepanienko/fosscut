package com.fosscut.alg;

import com.fosscut.shared.type.cutting.Element;

public class SingleOutput extends Element {

    private Integer id;
    private Integer relax;
    private Integer maxRelax;

    public SingleOutput(Integer id, Integer length, Integer relax, Integer maxRelax) {
        this.id = id;
        setLength(length);
        this.relax = relax;
        this.maxRelax = maxRelax;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(Integer relax) {
        this.relax = relax;
    }

    public Integer getMaxRelax() {
        return maxRelax;
    }

    public String toString() {
        return "FFDOutput: id = " + this.getId() + ", length = " + this.getLength() + ", relax = " + this.getRelax() + ", maxRelax = " + this.getMaxRelax();
    }

}
