package com.fosscut.alg.greedy;

import java.util.List;

import com.fosscut.alg.LPTask;
import com.fosscut.shared.type.cutting.order.OrderOutput;

public class GreedyLPTask extends LPTask {

    private List<OrderOutput> outputs;

    public List<OrderOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OrderOutput> outputs) {
        this.outputs = outputs;
    }

}
