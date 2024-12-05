package com.fosscut.alg.cg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.Order;
import com.fosscut.utils.Defaults;
import com.google.ortools.Loader;
import com.google.ortools.init.OrToolsVersion;

public class ColumnGeneration {
    private Double relaxCost;

    private Parameters param;

    public ColumnGeneration(Double relaxCost) {
        this.relaxCost = relaxCost;
    }

    public void run(Order order) {
        System.out.println("");
        System.out.println("Running cutting plan generation using column generation algorithm...");
        Loader.loadNativeLibraries();
        System.out.println("Google OR-Tools version: " + OrToolsVersion.getVersionString());

        param = new Parameters(order);

        double reducedCost;
        do {
            CuttingPlanGeneration linearCuttingPlanGeneration = new CuttingPlanGeneration(order, param, false);
            linearCuttingPlanGeneration.solve();

            PatternGeneration patternGeneration = new PatternGeneration(order, linearCuttingPlanGeneration.getDualValues(), relaxCost);
            patternGeneration.solve();
            reducedCost = patternGeneration.getObjective().value();

            param.incrementNPatternMax();
            if (relaxCost == null) copyPatterns(order, patternGeneration);
            else copyPatternsWithRelaxation(order, patternGeneration);

            // Default precision describes the smallest value of reducedCost
            // where further calculations are still sensible.
            // Rounds all digits below the default precision to zero.
        } while (
            BigDecimal.valueOf(reducedCost)
            .setScale(Defaults.CG_REDUCED_COST_PRECISION_PLACES, RoundingMode.FLOOR)
            .doubleValue() > 0
        );

        CuttingPlanGeneration integerCuttingPlanGeneration = new CuttingPlanGeneration(order, param, true);
        integerCuttingPlanGeneration.solve();
    }

    private void copyPatterns(Order order, PatternGeneration patternGeneration) {
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<Integer> outputsPattern = new ArrayList<>();
            for (int o = 0; o < order.getOutputs().size(); o++) {
                outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(i).get(o).solutionValue()).intValue());
            }
            param.getNipo().get(i).add(outputsPattern);
        }
    }

    private void copyPatternsWithRelaxation(Order order, PatternGeneration patternGeneration) {
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<Integer> outputsPattern = new ArrayList<>();
            List<Integer> relaxPattern = new ArrayList<>();
            for (int o = 0; o < order.getOutputs().size(); o++) {
                outputsPattern.add(Double.valueOf(patternGeneration.getUsageVariables().get(i).get(o).solutionValue()).intValue());
                relaxPattern.add(Double.valueOf(patternGeneration.getRelaxVariables().get(i).get(o).solutionValue()).intValue());
            }
            param.getNipo().get(i).add(outputsPattern);
            param.getRipo().get(i).add(relaxPattern);
        }
    }
}
