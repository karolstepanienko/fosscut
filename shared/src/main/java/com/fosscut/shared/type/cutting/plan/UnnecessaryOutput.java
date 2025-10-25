package com.fosscut.shared.type.cutting.plan;

import com.fosscut.shared.type.cutting.order.OrderOutput;

public class UnnecessaryOutput extends OrderOutput {

    private Integer id;

    public UnnecessaryOutput() {}

    public UnnecessaryOutput(Integer id,Integer length, Integer count, Integer maxRelax) {
        super(length, count, maxRelax);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
