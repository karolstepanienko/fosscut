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
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.util.Messages;
import com.google.ortools.Loader;

public class GreedyAlg extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(GreedyAlg.class);

    private Order order;
    private Double relaxCost;
    private boolean relaxEnabled;
    private RelaxationSpreadStrategy relaxationSpreadStrategy;
    private IntegerSolver integerSolver;
    private int integerNumThreads;
    private int patternGenerationFailureCount;

    public GreedyAlg(Order order, Double relaxCost, boolean relaxEnabled,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        OptimizationCriterion optimizationCriterion,
        IntegerSolver integerSolver,
        int integerNumThreads
    ) {
        super(optimizationCriterion);
        this.order = order;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
        this.integerSolver = integerSolver;
        this.integerNumThreads = integerNumThreads;
        this.patternGenerationFailureCount = 0;
    }

    public Plan getCuttingPlan(Long elapsedTimeMilliseconds) {
        return getCuttingPlan(order, elapsedTimeMilliseconds, patternGenerationFailureCount);
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
                try {
                    GreedyPatternGeneration greedyPatternGeneration =
                        new GreedyPatternGeneration(
                            inputId,
                            order.getInputs().get(inputId),
                            order.getOutputs(),
                            getOrderDemands(),
                            relaxCost,
                            relaxEnabled,
                            relaxationSpreadStrategy,
                            integerSolver,
                            integerNumThreads
                        );
                    greedyPatternGeneration.solve();
                    patterns.add(greedyPatternGeneration.getPattern());
                } catch (LPUnfeasibleException e) {
                    patternGenerationFailureCount++;
                    logger.warn(Messages.LP_UNFEASIBLE_WARNING_PART_1 + inputId + Messages.LP_UNFEASIBLE_WARNING_PART_2);
                }
            }
        }
        return patterns;
    }

}
