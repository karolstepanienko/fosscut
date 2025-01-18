package com.fosscut.alg.ch;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;

public class CHCuttingPlanFormatter {

    Order order;

    public CHCuttingPlanFormatter(Order order) {
        this.order = order;
    }

    public CuttingPlan getCuttingPlan(List<CHPattern> cuttingPlanPatterns) {
        return new CuttingPlan(
            getPlanInputs(cuttingPlanPatterns),
            order.getOutputs()
        );
    }

    private List<PlanInput> getPlanInputs(List<CHPattern> cuttingPlanPatterns) {
        List<PlanInput> inputs = new ArrayList<PlanInput>();

        for (OrderInput orderInput : order.getInputs()) {
            List<Pattern> patterns = collectPatternsForOrderInput(orderInput, cuttingPlanPatterns);
            if (!patterns.isEmpty()) {
                PlanInput input = new PlanInput();
                input.setLength(orderInput.getLength());
                input.setPatterns(patterns);
                inputs.add(input);
            }
        }

        return inputs;
    }

    private List<Pattern> collectPatternsForOrderInput(OrderInput orderInput, List<CHPattern> cuttingPlanPatterns) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (CHPattern pattern : cuttingPlanPatterns) {
            if (pattern.getInput().getLength() == orderInput.getLength()) {
                patterns.add(new Pattern(
                    pattern.getCount(),
                    pattern.getSerialisableRelaxPatternDefinition()
                ));
            }
        }
        return patterns;
    }

}
