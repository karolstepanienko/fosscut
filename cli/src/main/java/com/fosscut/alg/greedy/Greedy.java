package com.fosscut.alg.greedy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.google.ortools.Loader;

public class Greedy {

    private static final Logger logger = LoggerFactory.getLogger(Greedy.class);

    private Order order;

    public Greedy(Order order) {
        this.order = order;
    }

    public void run() {
        logger.info("");
        logger.info("Running cutting plan generation using a greedy algorithm...");

        Loader.loadNativeLibraries();

        for (OrderInput orderInput : order.getInputs()) {
            GreedyPatternGeneration greedyPatternGeneration = new GreedyPatternGeneration(order.getOutputs(), orderInput);
            greedyPatternGeneration.solve();
        }
    }

    private void demandLoop() {
        
    }

    private boolean isDemandSatisfied() {
        return true;
    }

}
