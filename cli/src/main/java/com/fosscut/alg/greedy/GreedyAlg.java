package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.google.ortools.Loader;

public class GreedyAlg extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(GreedyAlg.class);

    private Order order;
    private Double relaxCost;
    private boolean relaxEnabled;
    private RelaxationSpreadStrategy relaxationSpreadStrategy;
    private IntegerSolver integerSolver;

    public GreedyAlg(Order order, Double relaxCost, boolean relaxEnabled,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        OptimizationCriterion optimizationCriterion,
        IntegerSolver integerSolver
    ) {
        super(optimizationCriterion);
        this.order = order;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
        this.integerSolver = integerSolver;
    }

    public CuttingPlan getCuttingPlan() {
        return getCuttingPlan(
            order
        );
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
                        relaxEnabled,
                        relaxationSpreadStrategy,
                        integerSolver
                    );
                greedyPatternGeneration.solve();
                patterns.add(greedyPatternGeneration.getPattern());
            }
        }
        return patterns;
    }

}
