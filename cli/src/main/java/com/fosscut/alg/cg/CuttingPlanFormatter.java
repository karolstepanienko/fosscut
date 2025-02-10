package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;
import com.fosscut.type.cutting.plan.PlanOutput;
import com.fosscut.type.cutting.plan.PlanOutputDouble;
import com.fosscut.type.cutting.plan.PlanOutputInteger;

public class CuttingPlanFormatter {
    private Double relaxCost;
    private Order order;
    private Parameters params;
    private boolean forceIntegerRelax;

    public CuttingPlanFormatter(Double relaxCost, Order order, Parameters params, boolean forceIntegerRelax) {
        this.relaxCost = relaxCost;
        this.order = order;
        this.params = params;
        this.forceIntegerRelax = forceIntegerRelax;
    }

    public CuttingPlan getCuttingPlan(CuttingPlanGeneration integerCuttingPlanGeneration)
            throws NotIntegerLPTaskException {
        return new CuttingPlan(
            getPlanInputs(integerCuttingPlanGeneration),
            order.getOutputs()
        );
    }

    private List<PlanInput> getPlanInputs(CuttingPlanGeneration integerCuttingPlanGeneration)
            throws NotIntegerLPTaskException {
        List<List<Integer>> inputPatternUsage = integerCuttingPlanGeneration.getInputPatternUsage();
        List<PlanInput> planInputs = new ArrayList<>();

        for (int i = 0; i < order.getInputs().size(); i++) {
            planInputs.add(getPlanInput(inputPatternUsage, i));
        }

        return planInputs;
    }

    private PlanInput getPlanInput(List<List<Integer>> inputPatternUsage, int i) {
        PlanInput planInput = new PlanInput();
        planInput.setLength(order.getInputs().get(i).getLength());
        List<Pattern> patterns = new ArrayList<>();

        for (int p = 0; p < params.getNPatternMax(); p++) {
            Integer patternCount = inputPatternUsage.get(i).get(p);
            if (patternCount > 0 ) patterns.add(getPattern(patternCount, i, p));
        }
        planInput.setPatterns(patterns);
        return planInput;
    }

    private Pattern getPattern(Integer patternCount, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setCount(patternCount);
        List<PlanOutput> patternDefinition = new ArrayList<>();

        for (int o = 0; o < order.getOutputs().size(); o++) {
            Integer outputCount = params.getNipo().get(i).get(p).get(o);
            if (outputCount > 0) patternDefinition.add(getPlanOutput(outputCount, i, p, o));
        }
        pattern.setPatternDefinition(patternDefinition);
        return pattern;
    }

    private PlanOutput getPlanOutput(Integer outputCount, int i, int p, int o) {
        PlanOutput planOutput;
        if (relaxCost == null)
            planOutput = new PlanOutput(o, outputCount);
        else if (forceIntegerRelax)
            planOutput = new PlanOutputInteger(o, outputCount, params.getRipo().get(i).get(p).get(o).intValue());
        else
            planOutput = new PlanOutputDouble(o, outputCount, params.getRipo().get(i).get(p).get(o));

        return planOutput;
    }
}
