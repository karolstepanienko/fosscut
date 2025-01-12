package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.ffd.FFDPattern;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;

public class FFDCuttingPlanFormatter {

    Order order;

    public FFDCuttingPlanFormatter(Order order) {
        this.order = order;
    }

    public CuttingPlan getCuttingPlan(List<FFDPattern> cuttingPlanPatterns) {
        CuttingPlan cuttingPlan = new CuttingPlan();
        cuttingPlan.setInputs(getPlanInputs(cuttingPlanPatterns));
        cuttingPlan.setOutputs(order.getOutputs());
        return cuttingPlan;
    }

    private List<PlanInput> getPlanInputs(List<FFDPattern> cuttingPlanPatterns) {
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

    private List<Pattern> collectPatternsForOrderInput(OrderInput orderInput, List<FFDPattern> cuttingPlanPatterns) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (FFDPattern pattern : cuttingPlanPatterns) {
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
