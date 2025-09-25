package com.fosscut.alg.cg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.subcommand.abs.AbstractAlg;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.util.Defaults;
import com.google.ortools.Loader;

public class ColumnGeneration {

    private static final Logger logger = LoggerFactory.getLogger(ColumnGeneration.class);

    private Order order;
    private Double relaxCost;
    private boolean relaxEnabled;
    private OptimizationCriterion optimizationCriterion;
    private RelaxationSpreadStrategy relaxationSpreadStrategy;
    private LinearSolver linearSolver;
    private IntegerSolver integerSolver;

    private Parameters params;
    private CuttingPlanGeneration integerCuttingPlanGeneration;

    public ColumnGeneration(Order order, Double relaxCost,
        boolean relaxEnabled,
        OptimizationCriterion optimizationCriterion,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        LinearSolver linearSolver,
        IntegerSolver integerSolver
    ) {
        this.order = order;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.optimizationCriterion = optimizationCriterion;
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
        this.linearSolver = linearSolver;
        this.integerSolver = integerSolver;
    }

    public CuttingPlan getCuttingPlan() throws NotIntegerLPTaskException {
        CuttingPlanFormatter cuttingPlanFormatter = new CuttingPlanFormatter(
            AbstractAlg.isRelaxationEnabled(relaxEnabled, relaxCost),
            relaxationSpreadStrategy,
            order, params
        );
        return cuttingPlanFormatter.getCuttingPlan(integerCuttingPlanGeneration);
    }

    public void run() throws LPUnfeasibleException {
        logger.info("");
        logger.info("Running cutting plan generation using a column generation algorithm...");

        Loader.loadNativeLibraries();

        params = new Parameters(order);

        double previousLinearCuttingPlanObjectiveValue = Double.POSITIVE_INFINITY;
        double linearCuttingPlanObjectiveValue = Double.POSITIVE_INFINITY;
        double reducedCost = 0.0;
        do {
            previousLinearCuttingPlanObjectiveValue = linearCuttingPlanObjectiveValue;

            CuttingPlanGeneration linearCuttingPlanGeneration =
                new CuttingPlanGeneration(order, params, false,
                    optimizationCriterion, linearSolver, integerSolver);
            linearCuttingPlanGeneration.solve();
            linearCuttingPlanObjectiveValue = linearCuttingPlanGeneration.getObjective().value();
            List<Double> demandDualValues = linearCuttingPlanGeneration.getDemandDualValues();
            Map<Integer, Double> supplyDualValues = linearCuttingPlanGeneration.getSupplyDualValues();

            for (int inputId = 0; inputId < order.getInputs().size(); inputId++) {
                PatternGeneration patternGeneration = new PatternGeneration(
                    order, inputId, demandDualValues,
                    relaxCost, relaxEnabled, integerSolver
                );
                patternGeneration.solve();
                reducedCost += patternGeneration.getObjective().value();

                // TODO: check if pattern should be added

                if (AbstractAlg.isRelaxationEnabled(relaxEnabled, relaxCost)) {
                    copyPatternsWithRelaxation(order, inputId, patternGeneration);
                } else {
                    copyPatterns(order, inputId, patternGeneration);
                }
                params.incrementNumberOfPatternsPerInput(inputId);
            }

            // Default precision describes the smallest value of reducedCost
            // where further calculations are still sensible.
            // Rounds all digits below the default precision to zero.
        } while (
            BigDecimal.valueOf(reducedCost)
            .setScale(Defaults.CG_REDUCED_COST_PRECISION_PLACES, RoundingMode.FLOOR)
            .doubleValue() > 0
            && previousLinearCuttingPlanObjectiveValue > linearCuttingPlanObjectiveValue
        );

        integerCuttingPlanGeneration = new CuttingPlanGeneration(
            order, params, true, optimizationCriterion,
            linearSolver, integerSolver);
        integerCuttingPlanGeneration.solve();
    }

    private void copyPatterns(Order order, int inputId, PatternGeneration patternGeneration) {
        List<Integer> pattern = new ArrayList<>();
        for (int o = 0; o < order.getOutputs().size(); o++) {
            pattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(o).solutionValue()).intValue());
        }
        params.getNipo().get(inputId).add(pattern);
    }

    private void copyPatternsWithRelaxation(Order order, int inputId, PatternGeneration patternGeneration) {
        List<Integer> outputsPattern = new ArrayList<>();
        List<Integer> relaxPattern = new ArrayList<>();
        for (int o = 0; o < order.getOutputs().size(); o++) {
            outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(o).solutionValue()).intValue());
            relaxPattern.add(Double.valueOf(patternGeneration.getRelaxVariables().get(o).solutionValue()).intValue());
        }
        params.getNipo().get(inputId).add(outputsPattern);
        params.getRipo().get(inputId).add(relaxPattern);
    }

}
