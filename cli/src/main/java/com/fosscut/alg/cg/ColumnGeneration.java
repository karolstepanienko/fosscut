package com.fosscut.alg.cg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.IntegerSolvers;
import com.fosscut.type.LinearSolvers;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.util.Defaults;
import com.google.ortools.Loader;

public class ColumnGeneration {

    private static final Logger logger = LoggerFactory.getLogger(ColumnGeneration.class);

    private Order order;
    private Double relaxCost;
    private boolean forceIntegerRelax;
    private LinearSolvers linearSolver;
    private IntegerSolvers integerSolver;

    private Parameters params;
    private CuttingPlanGeneration integerCuttingPlanGeneration;

    public ColumnGeneration(Order order, Double relaxCost,
        boolean forceIntegerRelax,
        LinearSolvers linearSolver,
        IntegerSolvers integerSolver
    ) {
        this.order = order;
        this.relaxCost = relaxCost;
        this.forceIntegerRelax = forceIntegerRelax;
        this.linearSolver = linearSolver;
        this.integerSolver = integerSolver;
    }

    public void run() throws LPUnfeasibleException {
        logger.info("");
        logger.info("Running cutting plan generation using a column generation algorithm...");

        Loader.loadNativeLibraries();

        params = new Parameters(order);

        double previousLinearCuttingPlanObjectiveValue = Double.POSITIVE_INFINITY;
        double linearCuttingPlanObjectiveValue = Double.POSITIVE_INFINITY;
        double reducedCost;
        do {
            previousLinearCuttingPlanObjectiveValue = linearCuttingPlanObjectiveValue;

            CuttingPlanGeneration linearCuttingPlanGeneration =
                new CuttingPlanGeneration(order, params, false,
                    linearSolver, integerSolver);
            linearCuttingPlanGeneration.solve();
            linearCuttingPlanObjectiveValue = linearCuttingPlanGeneration.getObjective().value();

            PatternGeneration patternGeneration = new PatternGeneration(
                order, linearCuttingPlanGeneration.getDualValues(), relaxCost,
                forceIntegerRelax, integerSolver
            );
            patternGeneration.solve();
            reducedCost = patternGeneration.getObjective().value();

            params.incrementNPatternMax();
            if (relaxCost == null) copyPatterns(order, patternGeneration);
            else copyPatternsWithRelaxation(order, patternGeneration);

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
            order, params, true, linearSolver, integerSolver);
        integerCuttingPlanGeneration.solve();
    }

    public CuttingPlan getCuttingPlan() throws NotIntegerLPTaskException {
        CuttingPlanFormatter cuttingPlanFormatter = new CuttingPlanFormatter(relaxCost, order, params, forceIntegerRelax);
        return cuttingPlanFormatter.getCuttingPlan(integerCuttingPlanGeneration);
    }

    private void copyPatterns(Order order, PatternGeneration patternGeneration) {
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<Integer> outputsPattern = new ArrayList<>();
            for (int o = 0; o < order.getOutputs().size(); o++) {
                outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(i).get(o).solutionValue()).intValue());
            }
            params.getNipo().get(i).add(outputsPattern);
        }
    }

    private void copyPatternsWithRelaxation(Order order, PatternGeneration patternGeneration) {
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<Integer> outputsPattern = new ArrayList<>();
            List<Double> relaxPattern = new ArrayList<>();
            for (int o = 0; o < order.getOutputs().size(); o++) {
                outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(i).get(o).solutionValue()).intValue());
                relaxPattern.add(Double.valueOf(patternGeneration.getRelaxVariables().get(i).get(o).solutionValue()));
            }
            params.getNipo().get(i).add(outputsPattern);
            params.getRipo().get(i).add(relaxPattern);
        }
    }

}
