package com.fosscut.alg;

import com.fosscut.shared.type.cutting.Element;

public class SingleOutput extends Element {

    private Integer id;
    private Integer relax;

    public SingleOutput(Integer id, Integer length, Integer relax) {
        this.id = id;
        setLength(length);
        this.relax = relax;
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

    public String toString() {
        return "FFDOutput: id = " + this.getId() + ", length = " + this.getLength() + ", relax = " + this.getRelax();
    }

}
