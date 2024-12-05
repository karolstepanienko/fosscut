package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.Order;
import com.google.ortools.Loader;
import com.google.ortools.init.OrToolsVersion;

public class ColumnGeneration {
    private Parameters param;

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

            PatternGeneration patternGeneration = new PatternGeneration(order, linearCuttingPlanGeneration.getDualValues(), false);
            patternGeneration.solve();
            reducedCost = patternGeneration.getObjective().value();

            // Save new generated cutting patterns
            param.incrementNPatternMax();
            for (int i = 0; i < order.getInputs().size(); i++) {
                List<Integer> outputs = new ArrayList<>();
                List<Integer> relax = new ArrayList<>();
                for (int o = 0; o < order.getOutputs().size(); o++) {
                    outputs.add(Double.valueOf(patternGeneration.getUsageVariables().get(i).get(o).solutionValue()).intValue());
                    relax.add(Double.valueOf(patternGeneration.getRelaxVariables().get(i).get(o).solutionValue()).intValue());
                }
                param.getNipo().get(i).add(outputs);
                param.getRipo().get(i).add(relax);
            }
        } while (reducedCost > 0.0);

        CuttingPlanGeneration integerCuttingPlanGeneration = new CuttingPlanGeneration(order, param, true);
        integerCuttingPlanGeneration.solve();
    }
}
