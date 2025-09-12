package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.RelaxationSpread;
import com.fosscut.alg.SingleOutput;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;
import com.fosscut.type.cutting.plan.PlanOutput;

public class CuttingPlanFormatter {
    private boolean relaxEnabled;
    private Order order;
    private Parameters params;

    public CuttingPlanFormatter(boolean relaxEnabled, Order order, Parameters params) {
        this.relaxEnabled = relaxEnabled;
        this.order = order;
        this.params = params;
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
        planInput.setCost(order.getInputs().get(i).getCost());
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

        if (relaxEnabled) {
            pattern = getPatternWithRelax(patternCount, i, p);
        } else {
            pattern = getPatternWithoutRelax(patternCount, i, p);
        }

        return pattern;
    }

    private Pattern getPatternWithoutRelax(Integer patternCount, int i, int p) {
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

    private Pattern getPatternWithRelax(Integer patternCount, int i, int p) {
        Pattern pattern = new Pattern();
        pattern.setCount(patternCount);
        List<SingleOutput> singlePatternDefinition = new ArrayList<SingleOutput>();

        for (int outputId = 0; outputId < order.getOutputs().size(); outputId++) {
            int remainingSpace = 0;
            int numberOfRelaxedOutputs = 0;
            Integer outputCount = params.getNipo().get(i).get(p).get(outputId);
            OrderOutput output = order.getOutputs().get(outputId);

            if (output.getMaxRelax() != null && output.getMaxRelax() > 0) {
                numberOfRelaxedOutputs += outputCount;
                remainingSpace += outputCount * output.getMaxRelax();
                remainingSpace -= params.getRipo().get(i).get(p).get(outputId).intValue();
            }

            List<SingleOutput> singlePatternDefinitionForOneOutput
                = getSinglePatternDefinitionForOneOutput(outputId, outputCount, output);

            if (numberOfRelaxedOutputs > 0) {
                RelaxationSpreadStrategy relaxationSpreadStrategy = RelaxationSpreadStrategy.EQUAL;
                RelaxationSpread rss = new RelaxationSpread(relaxationSpreadStrategy);
                singlePatternDefinitionForOneOutput = rss.applyRelaxationSpread(
                    singlePatternDefinitionForOneOutput,
                    remainingSpace,
                    numberOfRelaxedOutputs
                );
            }

            singlePatternDefinition.addAll(singlePatternDefinitionForOneOutput);
        }

        pattern.setPatternDefinition(
            convertSingleToCGPatternDefinition(singlePatternDefinition)
        );
        return pattern;
    }

    private List<SingleOutput> getSinglePatternDefinitionForOneOutput(int outputId, Integer outputCount, OrderOutput output) {
        List<SingleOutput> singlePatternDefinition = new ArrayList<>();

        for (int j = 0; j < outputCount; ++j) {
            singlePatternDefinition.add(new SingleOutput(
                outputId,
                output.getLength(),
                output.getMaxRelax()
            ));
        }

        return singlePatternDefinition;
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
