package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.type.IntegerSolvers;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.google.ortools.Loader;

public class Greedy extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(Greedy.class);

    private Order order;
    private Double relaxCost;
    private boolean forceIntegerRelax;
    private IntegerSolvers integerSolver;

    public Greedy(Order order, Double relaxCost, boolean forceIntegerRelax,
        IntegerSolvers integerSolver
    ) {
        this.order = order;
        this.relaxCost = relaxCost;
        this.forceIntegerRelax = forceIntegerRelax;
        this.integerSolver = integerSolver;
    }

    public CuttingPlan getCuttingPlan() {
        boolean relaxEnabled = relaxCost != null;
        return getCuttingPlan(order, relaxEnabled, forceIntegerRelax);
    }

    public void run() {
        logger.info("");
        logger.info("Running cutting plan generation using a greedy algorithm...");

        initOrderDemands();

        Loader.loadNativeLibraries();

        setCuttingPlanPatterns(demandLoop());
    }

    private void initOrderDemands() {
        List<Integer> orderDemands = new ArrayList<Integer>();
        for (OrderOutput output : order.getOutputs()) {
            orderDemands.add(output.getCount());
        }
        setOrderDemands(orderDemands);
    }

    @Override
    protected List<CHPattern> generatePatternForEachInput() {
        List<CHPattern> patterns = new ArrayList<CHPattern>();
        for (OrderInput orderInput : order.getInputs()) {
            GreedyPatternGeneration greedyPatternGeneration =
                new GreedyPatternGeneration(
                    orderInput,
                    order.getOutputs(),
                    getOrderDemands(),
                    relaxCost,
                    forceIntegerRelax,
                    integerSolver
                );
            greedyPatternGeneration.solve();
            patterns.add(greedyPatternGeneration.getPattern());
        }
        return patterns;
    }

}
