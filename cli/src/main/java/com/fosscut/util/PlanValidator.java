package com.fosscut.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.PlanValidationException;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;

public class PlanValidator {

    private static final Logger logger = LoggerFactory.getLogger(PlanValidator.class);
    private OffendingPattern offendingPattern;

    private Integer expectedOutputCount;
    private Integer actualOutputCount;
    private OffendingOutput offendingOutput;

    public PlanValidator() {}

    public void validatePlan(CuttingPlan plan) throws PlanValidationException {
        logger.info("Running plan validation...");
        this.validate(plan);
        logger.info("Plan valid.");
    }

    private void validate(CuttingPlan plan) throws PlanValidationException {
        if (!patternsFitInInputs(plan)) throw new PlanValidationException(
            Messages.PLAN_PATTERN_DOES_NOT_FIT_IN_INPUT
            + " \n" + offendingPattern.toString()
        );
        else if (!isDemandSatisfied(plan)) throw new PlanValidationException(
            Messages.PLAN_DEMAND_NOT_SATISFIED
            + "\nExpected output count: " + expectedOutputCount
            + ", actual output count: " + actualOutputCount
            + ".\n" + offendingOutput.toString()
        );
    }

    /******************** Do patterns fit in inputs? **************************/

    private boolean patternsFitInInputs(CuttingPlan plan) {
        for (PlanInput input : plan.getInputs()) {
            if (!patternsFitInInput(input, plan.getOutputs())) {
                return false;
            }
        }
        return true;
    }

    private boolean patternsFitInInput(PlanInput input, List<OrderOutput> outputs) {
        for (Pattern pattern: input.getPatterns()) {
            if (!patternFitInInput(pattern, input, outputs)) {
                return false;
            }
        }
        return true;
    }

    private boolean patternFitInInput(Pattern pattern, PlanInput input, List<OrderOutput> outputs) {
        PatternLength patternLengthObj = calculatePatternLength(pattern, outputs);

        // Round up to get completely remove decimal part of pattern length to avoid precision issues
        BigDecimal patternLength = BigDecimal.valueOf(patternLengthObj.getLength())
            .setScale(0, RoundingMode.UP);

        if (patternLength.compareTo(BigDecimal.valueOf(input.getLength())) > 0) {
            this.offendingPattern = new OffendingPattern(input.getLength(), pattern);
            return false; // Pattern does not fit
        }
        return true;
    }

    private PatternLength calculatePatternLength(Pattern pattern, List<OrderOutput> outputs) {
        PatternLength patternLengthObj = new PatternLength();

        pattern.getPatternDefinition().forEach(planOutput -> {
            Integer outputLength = outputs.get(planOutput.getId()).getLength();

            Integer relaxedOutputLength = outputLength;
            Integer relaxValue = planOutput.getRelax();
            if (relaxValue != null) {
                relaxedOutputLength -= relaxValue;
            }

            Integer increment = planOutput.getCount() * relaxedOutputLength;
            patternLengthObj.setLength(patternLengthObj.getLength() + increment);
        });

        return patternLengthObj;
    }

    private class PatternLength {
        private Integer length;

        public PatternLength() {
            this.length = 0;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }
    }

    private class OffendingPattern {
        private Integer inputLength;
        private Pattern pattern;

        public OffendingPattern(Integer inputLength, Pattern pattern) {
            this.inputLength = inputLength;
            this.pattern = pattern;
        }

        public Integer getInputLength() {
            return inputLength;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String toString() {
            return new YamlDumper().dump(this);
        }
    }

    /*********************** Is demand satisfied? *****************************/

    private boolean isDemandSatisfied(CuttingPlan plan) {
        Map<Integer, Integer> expectedOutputDemands = new HashMap<>();
        for (int id = 0; id < plan.getOutputs().size(); id++) {
            expectedOutputDemands.put(id, plan.getOutputs().get(id).getCount());
        }

        Map<Integer, Integer> actualOutputDemands = new HashMap<>();
        plan.getInputs().forEach(input -> {
            input.getPatterns().forEach(pattern -> {
                pattern.getPatternDefinition().forEach(planOutput -> {
                    actualOutputDemands.merge(
                        planOutput.getId(),
                        pattern.getCount() * planOutput.getCount(),
                        Integer::sum
                    );
                });
            });
        });

        for (Map.Entry<Integer, Integer> entry : expectedOutputDemands.entrySet()) {
            Integer outputId = entry.getKey();
            Integer expectedCount = entry.getValue();
            Integer actualCount = actualOutputDemands.getOrDefault(outputId, 0);

            if (actualCount < expectedCount) {
                expectedOutputCount = expectedCount;
                actualOutputCount = actualCount;
                offendingOutput = new OffendingOutput(outputId, plan.getOutputs().get(outputId));
                return false;
            }
        }

        return true;
    }

    private class OffendingOutput {
        private Integer outputId;
        private OrderOutput output;

        public OffendingOutput(Integer outputId, OrderOutput output) {
            this.outputId = outputId;
            this.output = output;
        }

        public Integer getOutputId() {
            return outputId;
        }

        public OrderOutput getOutput() {
            return output;
        }

        public String toString() {
            return new YamlDumper().dump(this);
        }
    }

}
