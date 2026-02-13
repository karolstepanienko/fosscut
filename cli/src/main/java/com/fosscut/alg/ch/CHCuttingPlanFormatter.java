package com.fosscut.alg.ch;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.plan.Pattern;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.type.cutting.plan.PlanInput;
import com.fosscut.type.cutting.CHPattern;

public class CHCuttingPlanFormatter {

    private Order order;

    public CHCuttingPlanFormatter(Order order) {
        this.order = order;
    }

    public Plan getCuttingPlan(
        List<CHPattern> cuttingPlanPatterns,
        Long elapsedTimeMilliseconds,
        int patternGenerationFailureCount
    ) {
        return new Plan(
            getPlanInputs(cuttingPlanPatterns),
            order.getOutputs(),
            elapsedTimeMilliseconds,
            patternGenerationFailureCount
        );
    }

    private List<PlanInput> getPlanInputs(List<CHPattern> cuttingPlanPatterns) {
        List<PlanInput> inputs = new ArrayList<PlanInput>();

        for (OrderInput orderInput : order.getInputs()) {
            List<Pattern> patterns = collectPatternsForOrderInput(orderInput, cuttingPlanPatterns);
            PlanInput input = new PlanInput();
            input.setLength(orderInput.getLength());
            input.setCost(orderInput.getCost());
            input.setPatterns(patterns);
            inputs.add(input);
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
