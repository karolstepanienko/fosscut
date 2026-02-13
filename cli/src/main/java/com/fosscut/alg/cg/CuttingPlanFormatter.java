package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.RelaxationSpread;
import com.fosscut.alg.SingleOutput;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.plan.Pattern;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.type.cutting.plan.PlanInput;
import com.fosscut.shared.type.cutting.plan.PlanOutput;
import com.fosscut.type.RelaxationSpreadStrategy;

public class CuttingPlanFormatter {
    private boolean relaxEnabled;
    private Order order;
    private Parameters params;
    private Long elapsedTimeMilliseconds;
    private RelaxationSpread relaxationSpread;
    private int patternGenerationFailureCount;

    public CuttingPlanFormatter(
        boolean relaxEnabled,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        Order order,
        Parameters params,
        Long elapsedTimeMilliseconds,
        int patternGenerationFailureCount
    ) {
        this.relaxEnabled = relaxEnabled;
        this.order = order;
        this.params = params;
        this.elapsedTimeMilliseconds = elapsedTimeMilliseconds;
        this.relaxationSpread = new RelaxationSpread(relaxationSpreadStrategy);
        this.patternGenerationFailureCount = patternGenerationFailureCount;
    }

    public Plan getCuttingPlan(CuttingPlanGeneration integerCuttingPlanGeneration)
            throws NotIntegerLPTaskException {
        return new Plan(
            getPlanInputs(integerCuttingPlanGeneration),
            order.getOutputs(),
            elapsedTimeMilliseconds,
            patternGenerationFailureCount
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
        planInput.setCost(order.getInputs().get(i).getCost());
        List<Pattern> patterns = new ArrayList<>();

        for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
            Integer patternCount = inputPatternUsage.get(i).get(p);
            if (patternCount > 0 ) patterns.add(getPattern(patternCount, i, p));
        }
        planInput.setPatterns(patterns);
        return planInput;
    }

    private Pattern getPattern(Integer patternCount, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setCount(patternCount);

        if (relaxEnabled) {
            pattern = getPatternWithRelaxation(patternCount, i, p);
        } else {
            pattern = getPatternWithoutRelaxation(patternCount, i, p);
        }

        return pattern;
    }

    private Pattern getPatternWithoutRelaxation(Integer patternCount, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setCount(patternCount);
        List<PlanOutput> patternDefinition = new ArrayList<>();

        for (int outputId = 0; outputId < order.getOutputs().size(); outputId++) {
            Integer outputCount = params.getNipo().get(i).get(p).get(outputId);
            if (outputCount > 0) patternDefinition.add(new PlanOutput(outputId, outputCount, null));
        }

        pattern.setPatternDefinition(patternDefinition);
        return pattern;
    }

    private Pattern getPatternWithRelaxation(Integer patternCount, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setCount(patternCount);

        List<Integer> outputCounts = params.getNipo().get(i).get(p);
        List<Integer> relaxValues = params.getRipo().get(i).get(p);

        List<SingleOutput> singlePatternDefinition =
            relaxationSpread.getSinglePatternDefinition(
                order.getOutputs(),
                outputCounts,
                relaxValues
            );

        List<PlanOutput> cgPatternDefinition =
            convertSingleToCGPatternDefinition(
                singlePatternDefinition
            );

        pattern.setPatternDefinition(cgPatternDefinition);
        return pattern;
    }

    private List<PlanOutput> convertSingleToCGPatternDefinition(List<SingleOutput> singlePatternDefinition) {
        List<PlanOutput> cgPatternDefinition = new ArrayList<>();

        PlanOutput latest = null;
        for (SingleOutput singleOutput : singlePatternDefinition) {
            if (latest != null
                && singleOutput.getId().equals(latest.getId())
                && singleOutput.getRelax().equals(latest.getRelax().intValue())) {
                latest.setCount(latest.getCount() + 1);
            } else {
                cgPatternDefinition.add(
                    new PlanOutput(
                        singleOutput.getId(),
                        1,
                        singleOutput.getRelax()
                    )
                );
            }
            latest = cgPatternDefinition.getLast();
        }

        return cgPatternDefinition;
    }

}
