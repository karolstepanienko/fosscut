package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.type.IntegerSolvers;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
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

    public void run() throws GeneratedPatternsCannotBeEmptyException, LPUnfeasibleException {
        logger.info("");
        logger.info("Running cutting plan generation using a greedy algorithm...");

        initInputCounts(order);
        initOrderDemands(order);

        Loader.loadNativeLibraries();

        setCuttingPlanPatterns(demandLoop());
    }

    @Override
    protected List<CHPattern> generatePatternForEachInput() throws LPUnfeasibleException {
        List<CHPattern> patterns = new ArrayList<CHPattern>();
        for (Integer inputId = 0; inputId < order.getInputs().size(); ++inputId) {
            Integer inputCount = getInputCounts().get(inputId);
            if (inputCount == null || inputCount > 0) {
                GreedyPatternGeneration greedyPatternGeneration =
                    new GreedyPatternGeneration(
                        inputId,
                        order.getInputs().get(inputId),
                        order.getOutputs(),
                        getOrderDemands(),
                        relaxCost,
                        forceIntegerRelax,
                        integerSolver
                    );
                greedyPatternGeneration.solve();
                patterns.add(greedyPatternGeneration.getPattern());
            }
        }
        return patterns;
    }

}
