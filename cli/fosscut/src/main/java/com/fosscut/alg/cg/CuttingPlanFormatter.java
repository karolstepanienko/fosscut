package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
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
    private boolean integerRelax;

    public CuttingPlanFormatter(Double relaxCost, Order order, Parameters params, boolean integerRelax) {
        this.relaxCost = relaxCost;
        this.order = order;
        this.params = params;
        this.integerRelax = integerRelax;
    }

    public CuttingPlan getCuttingPlan(CuttingPlanGeneration integerCuttingPlanGeneration)
            throws NotIntegerLPTaskException {
        CuttingPlan cuttingPlan = new CuttingPlan();
        List<List<Integer>> inputPatternUsage = integerCuttingPlanGeneration.getInputPatternUsage();
        List<PlanInput> planInputs = new ArrayList<>();

        for (int i = 0; i < order.getInputs().size(); i++) {
            planInputs.add(getPlanInput(inputPatternUsage, i));
        }

        cuttingPlan.setInputs(planInputs);
        cuttingPlan.setOutputs(order.getOutputs());
        return cuttingPlan;
    }

    private PlanInput getPlanInput(List<List<Integer>> inputPatternUsage, int i) {
        PlanInput planInput = new PlanInput();
        planInput.setLength(order.getInputs().get(i).getLength());
        List<Pattern> patterns = new ArrayList<>();

        for (int p = 0; p < params.getNPatternMax(); p++) {
            Integer patternNumber = inputPatternUsage.get(i).get(p);
            if (patternNumber > 0 ) patterns.add(getPattern(patternNumber, i, p));
        }
        planInput.setPatterns(patterns);
        return planInput;
    }

    private Pattern getPattern(Integer patternNumber, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setNumber(patternNumber);
        List<PlanOutput> patternDefinition = new ArrayList<>();

        for (int o = 0; o < order.getOutputs().size(); o++) {
            Integer outputNumber = params.getNipo().get(i).get(p).get(o);
            if (outputNumber > 0) patternDefinition.add(getPlanOutput(outputNumber, i, p, o));
        }
        pattern.setPatternDefinition(patternDefinition);
        return pattern;
    }

    private PlanOutput getPlanOutput(Integer outputNumber, int i, int p, int o) {
        PlanOutput planOutput;
        if (relaxCost == null)
            planOutput = new PlanOutput(o, outputNumber);
        else if (integerRelax)
            planOutput = new PlanOutputInteger(o, outputNumber, params.getRipo().get(i).get(p).get(o).intValue());
        else
            planOutput = new PlanOutputDouble(o, outputNumber, params.getRipo().get(i).get(p).get(o));

        return planOutput;
    }
}
