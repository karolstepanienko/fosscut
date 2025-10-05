package com.fosscut.alg.cg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
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
    private boolean forceLinearImprovement;
    private LinearSolver linearSolver;
    private IntegerSolver integerSolver;

    private Parameters params;
    private CuttingPlanGeneration integerCuttingPlanGeneration;

    public ColumnGeneration(Order order, Double relaxCost,
        boolean relaxEnabled,
        OptimizationCriterion optimizationCriterion,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        boolean forceLinearImprovement,
        LinearSolver linearSolver,
        IntegerSolver integerSolver
    ) {
        this.order = order;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.optimizationCriterion = optimizationCriterion;
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
        this.forceLinearImprovement = forceLinearImprovement;
        this.linearSolver = linearSolver;
        this.integerSolver = integerSolver;
    }

    public CuttingPlan getCuttingPlan(Long elapsedTimeMilliseconds)
        throws NotIntegerLPTaskException
    {
        CuttingPlanFormatter cuttingPlanFormatter = new CuttingPlanFormatter(
            relaxEnabled,
            relaxationSpreadStrategy,
            order, params, elapsedTimeMilliseconds
        );
        return cuttingPlanFormatter.getCuttingPlan(integerCuttingPlanGeneration);
    }

    public void run() throws LPUnfeasibleException {
        logger.info("");
        logger.info("Running cutting plan generation using a column generation algorithm...");

        Loader.loadNativeLibraries();

        params = new Parameters(order);

        boolean patternsAdded = true;
        while (patternsAdded) {
            CuttingPlanGeneration linearCuttingPlanGeneration =
                new CuttingPlanGeneration(order, params, false,
                    optimizationCriterion, linearSolver, integerSolver,
                    relaxEnabled);
            linearCuttingPlanGeneration.solve();
            List<Double> demandDualValues = linearCuttingPlanGeneration.getDemandDualValues();
            Map<Integer, Double> supplyDualValues = linearCuttingPlanGeneration.getSupplyDualValues();

            patternsAdded = false;
            for (int inputId = 0; inputId < order.getInputs().size(); inputId++) {
                PatternGeneration patternGeneration = new PatternGeneration(
                    order, inputId, demandDualValues,
                    relaxCost, relaxEnabled, integerSolver
                );
                patternGeneration.solve();

                if (handleNewPattern(inputId, patternGeneration, supplyDualValues.get(inputId))) {
                    params.incrementNumberOfPatternsPerInput(inputId);
                    patternsAdded = true;
                }
            }
        }

        integerCuttingPlanGeneration = new CuttingPlanGeneration(
            order, params, true, optimizationCriterion,
            linearSolver, integerSolver, relaxEnabled);
        integerCuttingPlanGeneration.solve();
    }

    private boolean handleNewPattern(int inputId, PatternGeneration patternGeneration, Double supplyCost) {
        boolean patternShouldBeAdded = true;
        if (forceLinearImprovement) patternShouldBeAdded = doesPatternImproveLinearSolution(inputId, patternGeneration, supplyCost);

        boolean patternAdded = false;
        if (patternShouldBeAdded) {
            if (!relaxEnabled) {
                patternAdded = copyPattern(inputId, patternGeneration);
            } else {
                patternAdded = copyPatternWithRelaxation(inputId, patternGeneration);
            }
        }
        return patternAdded;
    }

    private boolean doesPatternImproveLinearSolution(int inputId, PatternGeneration patternGeneration, Double supplyCost) {
        double patternGain = patternGeneration.getObjective().value();
        double inputCost = getInputCost(inputId);
        double tolerance = inputCost / Defaults.CG_PATTERN_COST_COMPARISON_TOLERANCE_COEFFICIENT;
        if (supplyCost != null) inputCost -= supplyCost;

        // rounding to avoid precision issues
        BigDecimal bdPatternGain = BigDecimal.valueOf(patternGain).setScale(6, RoundingMode.HALF_UP);
        BigDecimal bdInputCost = BigDecimal.valueOf(inputCost).setScale(6, RoundingMode.HALF_UP).add(BigDecimal.valueOf(tolerance));
        return bdPatternGain.compareTo(bdInputCost) > 0;
    }

    private double getInputCost(int inputId) {
        double inputCost = 0.0;
        if (optimizationCriterion == OptimizationCriterion.MIN_COST
            && order.getInputs().get(inputId).getCost() != null
        ) {
            inputCost = order.getInputs().get(inputId).getCost().doubleValue();
        } else {
            inputCost = order.getInputs().get(inputId).getLength().doubleValue();
        }
        return inputCost;
    }

    private boolean copyPattern(int inputId, PatternGeneration patternGeneration) {
        boolean patternAdded = false;
        List<Integer> pattern = new ArrayList<>();
        for (int o = 0; o < order.getOutputs().size(); o++) {
            pattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(o).solutionValue()).intValue());
        }

        List<List<Integer>> patterns = params.getNipo().get(inputId);
        if (!patterns.contains(pattern)) {
            patterns.add(pattern);
            patternAdded = true;
        }
        return patternAdded;
    }

    private boolean copyPatternWithRelaxation(int inputId, PatternGeneration patternGeneration) {
        boolean patternAdded = false;
        List<Integer> outputsPattern = new ArrayList<>();
        List<Integer> relaxPattern = new ArrayList<>();
        for (int o = 0; o < order.getOutputs().size(); o++) {
            outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(o).solutionValue()).intValue());
            relaxPattern.add(Double.valueOf(patternGeneration.getRelaxVariables().get(o).solutionValue()).intValue());
        }

        List<List<Integer>> outputsPatterns = params.getNipo().get(inputId);
        List<List<Integer>> relaxPatterns = params.getRipo().get(inputId);
        if (!isRelaxedPatternAlreadyPresent(outputsPatterns, outputsPattern, relaxPatterns, relaxPattern)) {
            outputsPatterns.add(outputsPattern);
            relaxPatterns.add(relaxPattern);
            patternAdded = true;
        }
        return patternAdded;
    }

    private boolean isRelaxedPatternAlreadyPresent(
        List<List<Integer>> outputsPatterns, List<Integer> outputsPattern,
        List<List<Integer>> relaxPatterns, List<Integer> relaxPattern
    ) {
        List<Integer> allOutputIndices = IntStream.range(0, outputsPatterns.size())
            .filter(p -> outputsPatterns.get(p).equals(outputsPattern))
            .boxed()
            .collect(Collectors.toList());

        List<Integer> allRelaxIndices = IntStream.range(0, relaxPatterns.size())
            .filter(p -> relaxPatterns.get(p).equals(relaxPattern))
            .boxed()
            .collect(Collectors.toList());

        List<Integer> intersection = allOutputIndices.stream()
            .filter(allRelaxIndices::contains)
            .collect(Collectors.toList());

        // returns true if a matching pattern was found on at least one EQUAL index in both lists
        return !intersection.isEmpty();
    }

}
